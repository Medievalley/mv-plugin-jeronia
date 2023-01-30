package org.shrigorevich.ml.config;

import java.util.Map;

class EnemyPowerConfImpl implements EnemyPowerConf {
    private double playersFactor;

    public EnemyPowerConfImpl(Map<String, Object> map) {
        playersFactor = (double) map.getOrDefault("players_factor", "5432");
    }

    public double getPlayersFactor() {
        return playersFactor;
    }

    public void setPlayersFactor(double factor) {
        this.playersFactor = factor;
    }
}
