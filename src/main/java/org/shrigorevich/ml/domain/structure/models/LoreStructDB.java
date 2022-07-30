package org.shrigorevich.ml.domain.structure.models;

public interface LoreStructDB extends StructDB {
    int getVolumeId();
    void setVolumeId(int volumeId);
    String getName();
    void setName(String name);
    boolean isDestructible();
    void setDestructible(boolean destructible);
    int getBlocks();
    void setBlocks(int blocks);
    int getBrokenBlocks();
    void setBrokenBlocks(int brokenBlocks);
}
