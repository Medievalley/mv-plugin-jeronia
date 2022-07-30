package org.shrigorevich.ml.domain.structure.models;

public class VolumeBlockModel implements VolumeBlockDB {
    private int id;
    private int x;
    private int y;
    private int z;
    private String blockData;
    private String type;

    public VolumeBlockModel() {}
    public VolumeBlockModel(
            int x, int y, int z,
            String type, String blockData
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.blockData = blockData;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlockData() {
        return blockData;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }
}
