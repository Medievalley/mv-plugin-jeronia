package org.shrigorevich.ml.config;

import java.util.Map;

class MobSpawn {
    private int pressureInterval;
    private int waveInterval;
    private int maxMobQty;
    private double pressurePlayersFactor;

    public MobSpawn(Map<String, Object> map) {
        this.pressureInterval = (int) map.getOrDefault("pressure_interval", 0);
        this.waveInterval = (int) map.getOrDefault("wave_interval", 0);
        this.maxMobQty = (int) map.getOrDefault("max_mob_qty", 0);
        this.pressurePlayersFactor = (double) map.getOrDefault("pressure_players_factor", 0);
    }

    public int getPressureInterval() {
        return pressureInterval;
    }

    public int getWaveInterval() {
        return waveInterval;
    }

    public void setPressureInterval(int regSpawnInterval) {
        this.pressureInterval = regSpawnInterval;
    }

    public void setWaveInterval(int waveSpawnInterval) {
        this.waveInterval = waveSpawnInterval;
    }

    public double getPressurePlayersFactor() {
        return pressurePlayersFactor;
    }

    public void setPressurePlayersFactor(double factor) {
        this.pressurePlayersFactor = factor;
    }

    public int getMaxMobQty() {
        return maxMobQty;
    }

    public void setMaxMobQty(int maxMobQty) {
        this.maxMobQty = maxMobQty;
    }
}
