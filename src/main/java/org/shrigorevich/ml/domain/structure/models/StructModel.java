package org.shrigorevich.ml.domain.structure.models;

public interface StructModel {
    /* Base struct properties */
    int getId();
    void setId(int id);
    String getWorld();
    void setWorld(String world);
    int getTypeId();
    void setTypeId(int typeId);
    int getX1();
    int getY1();
    int getZ1();
    int getX2();
    int getY2();
    int getZ2();
    void setX1(int x1);
    void setY1(int y1);
    void setZ1(int z1);
    void setX2(int x2);
    void setY2(int y2);
    void setZ2(int z2);

    /* Specific struct properties */
    int getVolumeId();
    void setVolumeId(int volumeId);
    String getName();
    void setName(String name);
    int getBlocks();
    void setBlocks(int blocks);
    int getBrokenBlocks();
    void setBrokenBlocks(int brokenBlocks);
    int getPriority();
    int getDeposit();
    int getResources();
    void setDeposit(int deposit);
    void setResources(int resources);
}
