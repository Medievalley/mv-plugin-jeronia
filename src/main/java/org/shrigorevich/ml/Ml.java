package org.shrigorevich.ml;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.commands.StructureExecutor;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.db.DataSourceCreator;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.db.contexts.IUserContext;
import org.shrigorevich.ml.db.contexts.StructureContext;
import org.shrigorevich.ml.db.contexts.UserContext;
import org.shrigorevich.ml.domain.services.*;
import org.shrigorevich.ml.listeners.BlockBreak;
import org.shrigorevich.ml.listeners.BlockExplode;
import org.shrigorevich.ml.listeners.PlayerInteract;
import org.shrigorevich.ml.listeners.Auth;

import javax.sql.DataSource;

public final class Ml extends JavaPlugin {
    private Configuration config;
    private DataSource dataSource;
    private IUserService userService;
    private IStructureService structService;
    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Configuration(this);
        dataSource = DataSourceCreator.createDataSource(config);

        IUserContext userContext = new UserContext(this, dataSource);
        IStructureContext structureContext = new StructureContext(this, dataSource);
        userService = new UserService(userContext);
        structService = new StructureService(structureContext);
    }
    @Override
    public void onEnable() {
        setupListeners();
        setupExecutors();
        structService.loadStructures();
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
    }
}