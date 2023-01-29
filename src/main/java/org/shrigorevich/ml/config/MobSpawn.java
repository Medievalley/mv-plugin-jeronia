package org.shrigorevich.ml.config;

import java.util.Map;

class MobSpawn {
    public int regSpawnInterval;
    public int waveSpawnInterval;

    public MobSpawn(Map<String, Object> map) {
        regSpawnInterval = (int) map.getOrDefault("reg_spawn_interval", 1);
        waveSpawnInterval = (int) map.getOrDefault("wave_spawn_interval", 15);
    }
}
