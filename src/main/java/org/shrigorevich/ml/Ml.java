package org.shrigorevich.ml;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.admin.StructAdminService;
import org.shrigorevich.ml.admin.StructAdminServiceImpl;
import org.shrigorevich.ml.commands.NpcExecutor;
import org.shrigorevich.ml.commands.ScoreBoardExecutor;
import org.shrigorevich.ml.commands.StructureExecutor;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.common.DataSourceCreator;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.ai.TaskServiceImpl;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.mob.MobServiceImpl;
import org.shrigorevich.ml.domain.npc.contracts.NpcContext;
import org.shrigorevich.ml.domain.npc.NpcContextImpl;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.NpcServiceImpl;
import org.shrigorevich.ml.domain.project.contracts.ProjectContext;
import org.shrigorevich.ml.domain.project.ProjectContextImpl;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardServiceImpl;
import org.shrigorevich.ml.domain.structure.StructContextImpl;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.StructureServiceImpl;
import org.shrigorevich.ml.domain.users.contracts.UserService;
import org.shrigorevich.ml.domain.users.contracts.UserContext;
import org.shrigorevich.ml.domain.users.UserContextImpl;
import org.shrigorevich.ml.domain.users.UserServiceImpl;
import org.shrigorevich.ml.domain.project.contracts.ProjectService;
import org.shrigorevich.ml.domain.project.ProjectServiceImpl;
import org.shrigorevich.ml.listeners.*;

import javax.sql.DataSource;

public final class Ml extends JavaPlugin implements AdventurePlugin {

    private Configuration config;
    private DataSource dataSource;
    private UserService userService;
    private StructureService structService;
    private NpcService npcService;
    private TaskService taskService;
    private ProjectService projectService;
    private ScoreboardService scoreboardService;
    private MobService mobService;
    private StructAdminService structAdminService;

    private BukkitAudiences adventure;

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Configuration(this);
        dataSource = DataSourceCreator.createDataSource(config);

        UserContext userContext = new UserContextImpl(dataSource);
        StructureContext structureContext = new StructContextImpl(dataSource);
        NpcContext npcContext = new NpcContextImpl(dataSource);
        ProjectContext projectContext = new ProjectContextImpl(dataSource);

        userService = new UserServiceImpl(userContext, this);
        structService = new StructureServiceImpl(structureContext, this);
        npcService = new NpcServiceImpl(npcContext, this);
        taskService = new TaskServiceImpl(this);
        projectService = new ProjectServiceImpl(this, projectContext);
        scoreboardService = new ScoreboardServiceImpl(this);
        mobService = new MobServiceImpl(this);
        structAdminService = new StructAdminServiceImpl(this);
    }

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        super.onEnable();
        setupListeners();
        setupExecutors();
        try {
            structService.load();
            npcService.load();
            projectService.load();
            mobService.load();
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
        }
    }
    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        try {
            npcService.unload();
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
        }
        System.out.println("DISABLED");
    }

    private void setupListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Auth(userService), this);
        pm.registerEvents(new BlockBreak(structService), this);
        pm.registerEvents(new CustomSpawn(taskService, npcService, structService), this);
        pm.registerEvents(new EntityInventoryHandler(npcService, projectService), this);
        pm.registerEvents(new EntitySpawn(this), this);
        pm.registerEvents(new HarvestHandler(taskService, npcService, structService), this);
        pm.registerEvents(new PlantGrow(structService), this);
        pm.registerEvents(new PlayerInteract(structService, npcService, taskService), this);
        pm.registerEvents(new ReachLocationHandler(this), this);
        pm.registerEvents(new DangerHandler(taskService, npcService), this);
        pm.registerEvents(new EntityDeathHandler(npcService, projectService, taskService, structService), this);
        pm.registerEvents(new BuildProjectHandler(projectService, scoreboardService, npcService, taskService, structService), this);
    }

    private void setupExecutors() {
        getCommand("struct").setExecutor(new StructureExecutor(userService, structService, projectService, structAdminService));
        getCommand("npc").setExecutor(new NpcExecutor(npcService, taskService, mobService));
        getCommand("score").setExecutor(new ScoreBoardExecutor(scoreboardService));
    }

    @Override
    public void showTitle(String title, String subTitle) {
        showTitle(title, subTitle, Color.BLUE.asRGB());
    }

    @Override
    public void showTitle(String title, String subTitle, int color) {
        showTitle(title, subTitle, color, color);
    }

    @Override
    public void showTitle(String title, String subTitle, int titleColor, int subTitleColor) {
        Audience audience = this.adventure().players();
        Title titleToShow = Title.title(
                Component.text(title).color(TextColor.color(titleColor)),
                Component.text(subTitle).color(TextColor.color(subTitleColor))
        );
        audience.showTitle(titleToShow);
    }
}