package org.shrigorevich.ml;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.db.DataSourceCreator;
import org.shrigorevich.ml.db.contexts.IUserContext;
import org.shrigorevich.ml.db.contexts.UserContext;
import org.shrigorevich.ml.domain.services.IUserService;
import org.shrigorevich.ml.domain.services.UserService;
import org.shrigorevich.ml.listeners.PreLogin;

import javax.sql.DataSource;

public final class Ml extends JavaPlugin {
    private Configuration config;
    private DataSource dataSource;
    private IUserService userService;
    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Configuration(this);
        dataSource = DataSourceCreator.createDataSource(config);

        IUserContext userContext = new UserContext(this, dataSource);
        userService = new UserService(userContext);
    }
    @Override
    public void onEnable() {
        setupListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DISABLED");
    }

    public void setupListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PreLogin(userService), this);
    }
}