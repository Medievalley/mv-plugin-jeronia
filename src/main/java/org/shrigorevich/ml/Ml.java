package org.shrigorevich.ml;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.config.Configuration;
import org.shrigorevich.ml.config.Database;
import org.shrigorevich.ml.db.DataSourceCreator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Ml extends JavaPlugin {
    Configuration config;
    DataSource dataSource;
    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Configuration(this);
        dataSource = DataSourceCreator.createDataSource(config);
    }
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DISABLED");
    }
}