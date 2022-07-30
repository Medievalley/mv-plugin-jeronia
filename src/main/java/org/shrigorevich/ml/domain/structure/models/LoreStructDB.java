package org.shrigorevich.ml.domain.structure.models;

public interface LoreStructDB extends StructDB {
    int getDestroyedPercent();
    void setDestroyedPercent(int destroyedPercent);
    int getVolumeId();
    void setVolumeId(int volumeId);
    String getName();
    void setName(String name);
    boolean isDestructible();
    void setDestructible(boolean destructible);
}
