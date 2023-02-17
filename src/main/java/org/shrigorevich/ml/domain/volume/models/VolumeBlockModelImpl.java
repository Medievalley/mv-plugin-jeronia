package org.shrigorevich.ml.domain.volume.models;

public class VolumeBlockModelImpl implements VolumeBlockModel {
    private int id;
    private int x;
    private int y;
    private int z;
    private String blockData, material;

    public VolumeBlockModelImpl() {}
    public VolumeBlockModelImpl(
            int x, int y, int z,
            String material, String blockData
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
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

    public String getBlockData() {
        return blockData;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }
    public String getMaterial() {
        return material;
    }
    @Override
    public void setMaterial(String material) {
        this.material = material;
    }
}
