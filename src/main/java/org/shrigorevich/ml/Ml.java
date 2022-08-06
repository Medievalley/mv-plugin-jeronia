package org.shrigorevich.ml;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.commands.NpcExecutor;
import org.shrigorevich.ml.commands.StructureExecutor;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.db.DataSourceCreator;
import org.shrigorevich.ml.db.contexts.*;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.NpcServiceImpl;
import org.shrigorevich.ml.domain.services.*;
import org.shrigorevich.ml.domain.users.IUserService;
import org.shrigorevich.ml.domain.users.UserService;
import org.shrigorevich.ml.listeners.BlockBreak;
import org.shrigorevich.ml.listeners.BlockExplode;
import org.shrigorevich.ml.listeners.PlayerInteract;
import org.shrigorevich.ml.listeners.Auth;

import javax.sql.DataSource;

public final class Ml extends JavaPlugin {
    private Configuration config;
    private DataSource dataSource;
    private IUserService userService;
    private StructureService structService;
    private NpcService npcService;
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
        npcService = new NpcServiceImpl(npcContext);

    }
    @Override
    public void onEnable() {
        setupListeners();
        setupExecutors();
        try {
            structService.loadStructures();
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DISABLED");
    }

    private void setupListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Auth(userService), this);
        pm.registerEvents(new PlayerInteract(structService), this);
        pm.registerEvents(new BlockExplode(structService), this);
        pm.registerEvents(new BlockBreak(structService), this);
    }

    private void setupExecutors() {
        getCommand("struct").setExecutor(new StructureExecutor(userService, structService));
        getCommand("npc").setExecutor(new NpcExecutor(npcService));
    }
}