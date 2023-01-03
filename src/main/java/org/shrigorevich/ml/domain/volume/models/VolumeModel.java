package org.shrigorevich.ml.domain.volume.models;

public interface VolumeModel {
    String getName();
    int getId();
    int getSizeX();
    int getSizeY();
    int getSizeZ();
    void setName(String name);
    void setId(int id);
    void setSizeX(int sizeX);
    void setSizeY(int sizeY);
    void setSizeZ(int sizeZ);
}
