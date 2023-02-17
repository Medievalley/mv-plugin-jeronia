package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

public interface MlConfiguration {

    int getPressureInterval();
    void setPressureInterval(int value);
    double getPressurePlayersFactor();
    void setPressurePlayersFactor(double value);
    int getMaxMobQty();
    void setMaxMobQty(int maxMobQty);
    DatabaseConf getDb();
    void reload();
    void save();
}
