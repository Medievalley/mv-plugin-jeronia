package org.shrigorevich.ml;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.commands.NpcExecutor;
import org.shrigorevich.ml.commands.StructureExecutor;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.db.DataSourceCreator;
import org.shrigorevich.ml.db.contexts.*;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskServiceImpl;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.NpcServiceImpl;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.StructureServiceImpl;
import org.shrigorevich.ml.domain.users.IUserService;
import org.shrigorevich.ml.domain.users.UserService;
import org.shrigorevich.ml.listeners.*;

import javax.sql.DataSource;

public final class Ml extends JavaPlugin {
    private Configuration config;
    private DataSource dataSource;
    private IUserService userService;
    private StructureService structService;
    private NpcService npcService;
    private TaskService goalService;
    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Configuration(this);
        dataSource = DataSourceCreator.createDataSource(config);

        UserContext userContext = new UserContextImpl(this, dataSource);
        StructureContext structureContext = new StructContextImpl(this, dataSource);
        NpcContext npcContext = new NpcContextImpl(this, dataSource);

        userService = new UserService(userContext);
        structService = new StructureServiceImpl(structureContext, this);
        npcService = new NpcServiceImpl(npcContext, this);
        goalService = new TaskServiceImpl(this);
    }
    @Override
    public void onEnable() {
        super.onEnable();
        setupListeners();
        setupExecutors();
        try {
            structService.load();
            npcService.load();
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DISABLED");
        npcService.unload();
    }

    private void setupListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Auth(userService), this);
        pm.registerEvents(new PlayerInteract(structService, npcService), this);
        pm.registerEvents(new BlockExplode(structService), this);
        pm.registerEvents(new BlockBreak(structService), this);
        pm.registerEvents(new CustomSpawn(goalService, npcService, structService), this);
        pm.registerEvents(new HarvestHandler(goalService), this);
        pm.registerEvents(new PlantGrow(structService), this);
    }

    private void setupExecutors() {
        getCommand("struct").setExecutor(new StructureExecutor(userService, structService));
        getCommand("npc").setExecutor(new NpcExecutor(npcService));
    }
}