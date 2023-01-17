package org.shrigorevich.ml.domain.volume.models;

public interface VolumeBlockModel {
    int getId();
    void setId(int id);
    String getMaterial();
    void setMaterial(String material);
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
    int getZ();
    void setZ(int z);
    String getBlockData();
    void setBlockData(String blockData);
}
