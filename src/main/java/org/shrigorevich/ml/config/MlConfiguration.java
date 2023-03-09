package org.shrigorevich.ml.config;

public interface MlConfiguration {

    int getMaxJobsQty();
    void setMaxJobsQty(int value);
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
