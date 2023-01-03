package org.shrigorevich.ml.domain.structure.models;

public interface LoreStructModel extends StructModel {
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
    int getStock();
    void setStock(int stock);
    int getPriority();
}
