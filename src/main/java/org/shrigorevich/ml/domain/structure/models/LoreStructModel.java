package org.shrigorevich.ml.domain.structure.models;

public class LoreStructModel extends StructModel implements LoreStructDB {
    public int volumeId;
    /** ID of base structure */
    public String name;
    public boolean destructible;
    public int blocks, brokenBlocks, stock;
    public LoreStructModel() {
        super();
    }

    public int getVolumeId() {
        return volumeId;
    }
    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isDestructible() {
        return destructible;
    }
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }
    public int getBlocks() {
        return blocks;
    }
    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }
    public int getBrokenBlocks() {
        return brokenBlocks;
    }
    public void setBrokenBlocks(int brokenBlocks) {
        this.brokenBlocks = brokenBlocks;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public void setStock(int stock) {
        this.stock = stock;
    }
}
