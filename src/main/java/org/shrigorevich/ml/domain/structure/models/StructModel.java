package org.shrigorevich.ml.domain.structure.models;

public interface StructModel {
    /* Base struct properties */
    int getId();
    void setId(int id);
    String getWorld();
    void setWorld(String world);
    int getTypeId();
    int getX1();
    int getY1();
    int getZ1();
    int getX2();
    int getY2();
    int getZ2();

    /* Specific struct properties */
    int getVolumeId();
    void setVolumeId(int volumeId);
    String getName();
    void setName(String name);
    int getBlocks();
    void setBlocks(int blocks);
    int getBrokenBlocks();
    int getPriority();
    int getResources();
    void setResources(int resources);
    int getDeposit();
    void setDeposit(int deposit);
}
