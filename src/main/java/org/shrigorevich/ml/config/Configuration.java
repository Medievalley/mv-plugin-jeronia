package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

public class Configuration {
    private final Plugin plugin;

    private Database database;

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        database = new Database(plugin.getConfig().getConfigurationSection("database").getValues(false));
        System.out.println("My Database user: " + database.getUser());
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Database getDatabase() {
        return database;
    }
}