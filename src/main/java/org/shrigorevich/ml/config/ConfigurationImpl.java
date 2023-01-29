package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ConfigurationImpl implements MlConfiguration {
    private final Plugin plugin;

    private DatabaseConf database;
    private MobSpawn mobSpawn;

    public ConfigurationImpl(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.database = new DatabaseConfImpl(Objects.requireNonNull(
                plugin.getConfig().getConfigurationSection("database")).getValues(false));
        this.mobSpawn = new MobSpawn(Objects.requireNonNull(
                plugin.getConfig().getConfigurationSection("mob_spawn")).getValues(false));

    }

    public void updateRegSpawnInterval(int value) {
        mobSpawn.regSpawnInterval = value;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public DatabaseConf getDb() {
        return database;
    }
}