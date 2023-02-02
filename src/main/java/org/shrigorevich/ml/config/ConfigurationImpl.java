package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ConfigurationImpl implements MlConfiguration {
    private final Plugin plugin;

    private EnemyPowerConf enemyPowerConf;
    private DatabaseConf database;
    private MobSpawn mobSpawn;

    public ConfigurationImpl(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.enemyPowerConf = new EnemyPowerConfImpl(Objects.requireNonNull(
                plugin.getConfig().getConfigurationSection("enemy_power")).getValues(false));
        this.database = new DatabaseConfImpl(Objects.requireNonNull(
            plugin.getConfig().getConfigurationSection("database")).getValues(false));
        this.mobSpawn = new MobSpawn(Objects.requireNonNull(
            plugin.getConfig().getConfigurationSection("mob_spawn")).getValues(false));
    }

    //MOB SPAWN START
    @Override
    public void updateRegSpawnInterval(int value) {
        mobSpawn.setRegSpawnInterval(value);
    }
    @Override
    public int getRegSpawnInterval() {
        return mobSpawn.getRegSpawnInterval();
    }
    @Override
    public int getMaxMobQty() {
        return mobSpawn.getMaxMobQty();
    }
    //MOB SPAWN END

    //ENEMY POWER START
    @Override
    public double getRegSpawnPlayersFactor() {
        return enemyPowerConf.getPlayersFactor();
    }
    //ENEMY POWER END

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public DatabaseConf getDb() {
        return database;
    }
}