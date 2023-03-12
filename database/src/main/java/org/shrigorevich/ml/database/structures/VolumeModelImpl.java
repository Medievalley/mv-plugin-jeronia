package org.shrigorevich.ml.database.structures;

import org.shrigorevich.ml.state.structures.models.VolumeModel;

public class VolumeModelImpl implements VolumeModel {
    private int id;
    private String name;
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    public VolumeModelImpl(String name, int sizeX, int sizeY, int sizeZ) {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }
    public VolumeModelImpl(){}
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public int getSizeX() {
        return sizeX;
    }
    public int getSizeY() {
        return sizeY;
    }
    public int getSizeZ() {
        return sizeZ;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }
    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }
}
