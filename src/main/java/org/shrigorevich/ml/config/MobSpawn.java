package org.shrigorevich.ml.config;

import java.util.Map;

class MobSpawn {
    private int regSpawnInterval;
    private int waveSpawnInterval;
    private int maxMobQty;

    public MobSpawn(Map<String, Object> map) {
        regSpawnInterval = (int) map.getOrDefault("reg_spawn_interval", 1);
        waveSpawnInterval = (int) map.getOrDefault("wave_spawn_interval", 15);
        maxMobQty = (int) map.getOrDefault("max_mob_qty", 50);
    }

    public int getRegSpawnInterval() {
        return regSpawnInterval;
    }

    public int getWaveSpawnInterval() {
        return waveSpawnInterval;
    }

    public void setRegSpawnInterval(int regSpawnInterval) {
        this.regSpawnInterval = regSpawnInterval;
    }

    public void setWaveSpawnInterval(int waveSpawnInterval) {
        this.waveSpawnInterval = waveSpawnInterval;
    }

    public int getMaxMobQty() {
        return maxMobQty;
    }
}
