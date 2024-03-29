package org.shrigorevich.ml;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.common.config.MlConfiguration;
import org.shrigorevich.ml.config.ConfigurationImpl;
import org.shrigorevich.ml.database.DataSourceCreator;
import org.shrigorevich.ml.database.npc.NpcContextImpl;
import org.shrigorevich.ml.database.project.ProjectContextImpl;
import org.shrigorevich.ml.database.structures.StructContextImpl;
import org.shrigorevich.ml.database.users.UserContextImpl;
import org.shrigorevich.ml.domain.MlPlugin;
import org.shrigorevich.ml.domain.admin.*;
import org.shrigorevich.ml.domain.admin.handlers.AdminInteractHandler;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskServiceImpl;
import org.shrigorevich.ml.domain.commands.*;
import org.shrigorevich.ml.domain.handlers.*;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.structures.StructureService;
import org.shrigorevich.ml.domain.users.UserService;
import org.shrigorevich.ml.state.mobs.MobServiceImpl;
import org.shrigorevich.ml.state.npc.NpcContext;
import org.shrigorevich.ml.state.npc.NpcServiceImpl;
import org.shrigorevich.ml.state.project.ProjectContext;
import org.shrigorevich.ml.state.project.ProjectServiceImpl;
import org.shrigorevich.ml.state.scoreboard.ScoreboardServiceImpl;
import org.shrigorevich.ml.state.structures.StructureContext;
import org.shrigorevich.ml.state.structures.StructureServiceImpl;
import org.shrigorevich.ml.state.users.UserContext;
import org.shrigorevich.ml.state.users.UserServiceImpl;

import javax.sql.DataSource;
import java.util.Objects;

public final class Ml extends JavaPlugin implements MlPlugin {

    private UserService userService;
    private StructureService structService;
    private NpcService npcService;
    private TaskService taskService;
    private ProjectService projectService;
    private ScoreboardService scoreboardService;
    private MobService mobService;
    private StructAdminService structAdminService;
    private NpcAdminService npcAdminService;
    private BukkitAudiences adventure;
    private MlConfiguration config;

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();

        this.config = new ConfigurationImpl(this);
        DataSource dataSource = DataSourceCreator.createDataSource(config);

        UserContext userContext = new UserContextImpl(dataSource);
        StructureContext structureContext = new StructContextImpl(dataSource);
        NpcContext npcContext = new NpcContextImpl(dataSource);
        ProjectContext projectContext = new ProjectContextImpl(dataSource);

        userService = new UserServiceImpl(userContext, this, config);
        structService = new StructureServiceImpl(structureContext, this);
        npcService = new NpcServiceImpl(npcContext, this);
        taskService = new TaskServiceImpl(this);
        projectService = new ProjectServiceImpl(this, projectContext);
        scoreboardService = new ScoreboardServiceImpl(this);
        mobService = new MobServiceImpl(this);
        structAdminService = new StructAdminServiceImpl();
        npcAdminService = new NpcAdminServiceImpl(this);
    }

    @Override
    public void onEnable() {
//        this.adventure = BukkitAudiences.create(this);
        super.onEnable();
        setupListeners();
        setupExecutors();
        setupState();
    }

    @Override
    public void onDisable() {
//        if(this.adventure != null) {
//            this.adventure.close();
//            this.adventure = null;
//        }
        try {
            npcService.unload();
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
        }
        System.out.println("DISABLED");
    }

    private void setupListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AuthHandler(userService), this);
        pm.registerEvents(new BlockBreak(structService), this);
        pm.registerEvents(new CustomSpawn(taskService, npcService, structService, mobService), this);
        pm.registerEvents(new EntityInventoryHandler(npcService, projectService), this);
        pm.registerEvents(new EntitySpawnHandler(this, mobService), this);
        pm.registerEvents(new PlayerInteract(structService, npcService, taskService), this);
        pm.registerEvents(new ReachLocationHandler(this), this);
        pm.registerEvents(new DangerHandler(taskService, npcService), this);
        pm.registerEvents(new EntityDeathHandler(npcService, projectService, taskService, structService, userService), this);
        pm.registerEvents(new BuildProjectHandler(projectService, scoreboardService, npcService, taskService, structService), this);
        pm.registerEvents(new SetupStateHandler(structService, projectService, npcService, mobService, scoreboardService, userService), this);
        pm.registerEvents(new AdminInteractHandler(structAdminService, structService, npcService, npcAdminService, userService, this), this);
        pm.registerEvents(new SpawnTimersHandler(config, this), this);
        pm.registerEvents(new SpawnEnemyHandler(structService, mobService, config, this), this);
        pm.registerEvents(new PlayerDeathHandler(userService), this);
        pm.registerEvents(new ExploreEnvironmentHandler(this, structService), this);
        pm.registerEvents(new KnockDoorHandler(), this);
    }

    private void setupExecutors() {
        Objects.requireNonNull(getCommand("struct"))
            .setExecutor(new StructureExecutor(userService, structService, projectService, structAdminService));
        Objects.requireNonNull(getCommand("npc"))
            .setExecutor(new NpcExecutor(npcService, npcAdminService, taskService, mobService));
        Objects.requireNonNull(getCommand("score"))
            .setExecutor(new ScoreBoardExecutor(scoreboardService));
        Objects.requireNonNull(getCommand("config"))
            .setExecutor(new ConfigExecutor(config));
        Objects.requireNonNull(getCommand("mob"))
            .setExecutor(new MobExecutor(taskService, mobService, structService));
        Objects.requireNonNull(getCommand("test"))
            .setExecutor(new TestExecutor(config, this));
        Objects.requireNonNull(getCommand("job"))
                .setExecutor(new JobExecutor(userService));
    }

    //TODO: move to separate class
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

    private void setupState() {
        try {
            Bukkit.getPluginManager().callEvent(new SetupStateEvent()); //TODO: Maybe move to command executor
        } catch (Exception ex) {
            //TODO: inject logger
            Bukkit.getLogger().severe(ex.getMessage());
        }
    }

    @Override
    public BukkitScheduler getScheduler() {
        return getServer().getScheduler();
    }
    @Override
    public void callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }
}