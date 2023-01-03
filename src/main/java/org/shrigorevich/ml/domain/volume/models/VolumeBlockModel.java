package org.shrigorevich.ml.domain.volume.models;

public interface VolumeBlockModel {
    int getId();
    void setId(int id);
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
    int getZ();
    void setZ(int z);
    String getType();
    void setType(String type);
    String getBlockData();
    void setBlockData(String blockData);
}
