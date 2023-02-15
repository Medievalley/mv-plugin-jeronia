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

    //MOB SPAWN START
    @Override
    public void setPressureInterval(int value) {
        mobSpawn.setPressureInterval(value);
    }
    @Override
    public int getPressureInterval() {
        return mobSpawn.getPressureInterval();
    }
    @Override
    public int getMaxMobQty() {
        return mobSpawn.getMaxMobQty();
    }
    @Override
    public void setMaxMobQty(int maxMobQty) {
        mobSpawn.setMaxMobQty(maxMobQty);
    }
    @Override
    public double getPressurePlayersFactor() {
        return mobSpawn.getPressurePlayersFactor();
    }

    @Override
    public void setPressurePlayersFactor(double value) {
        this.mobSpawn.setPressurePlayersFactor(value);
    }
    //MOB SPAWN END

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public DatabaseConf getDb() {
        return database;
    }
}