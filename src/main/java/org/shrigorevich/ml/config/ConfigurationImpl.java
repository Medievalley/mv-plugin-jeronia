package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

public class ConfigurationImpl {
    private final Plugin plugin;

    private Database database;
    private MobSpawn mobSpawn;

    public ConfigurationImpl(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        database = new Database(plugin.getConfig().getConfigurationSection("database").getValues(false));
        System.out.println("My Database user: " + database.getUser());
    }

    public void updateRegSpawnInterval(int value) {
        mobSpawn.regSpawnInterval = value;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Database getDatabase() {
        return database;
    }
}