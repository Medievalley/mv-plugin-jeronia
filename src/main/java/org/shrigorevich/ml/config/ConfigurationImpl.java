package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ConfigurationImpl implements MlConfiguration {
    private final Plugin plugin;
    private DatabaseConf database;
    private MobSpawn mobSpawn;
    private UserConf user;

    public ConfigurationImpl(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.database = new DatabaseConfImpl(Objects.requireNonNull(
            plugin.getConfig().getConfigurationSection("database")).getValues(false));
        this.mobSpawn = new MobSpawn(Objects.requireNonNull(
            plugin.getConfig().getConfigurationSection("mob_spawn")).getValues(false));
        this.user = new UserConf(Objects.requireNonNull(
            plugin.getConfig().getConfigurationSection("user")).getValues(false));
    }

    @Override
    public void save() {
        plugin.saveConfig();
    }

    @Override
    public DatabaseConf getDb() {
        return database;
    }
    //MOB SPAWN START
    @Override
    public double getPressurePlayersFactor() {
        return mobSpawn.getPressurePlayersFactor();
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
    public void setPressureInterval(int value) {
        plugin.getConfig().set(ConfPath.MOB_SPAWN_PRESSURE_INTERVAL.getPath(), value);
        mobSpawn.setPressureInterval(value);
    }
    @Override
    public void setMaxMobQty(int value) {
        plugin.getConfig().set(ConfPath.MOB_SPAWN_MAX_MOB_QTY.getPath(), value);
        mobSpawn.setMaxMobQty(value);
    }

    @Override
    public void setPressurePlayersFactor(double value) {
        plugin.getConfig().set(ConfPath.MOB_SPAWN_PRESSURE_PF.getPath(), value);
        this.mobSpawn.setPressurePlayersFactor(value);
    }
    //MOB SPAWN END
    //USER START
    public int getMaxJobsQty() { return user.getMaxJobsQty(); }
    public void setMaxJobsQty(int value) {
        plugin.getConfig().set(ConfPath.USER_MAX_JOBS_QTY.getPath(), value);
        this.user.setMaxJobsQty(value);
    }
    //USER END
}