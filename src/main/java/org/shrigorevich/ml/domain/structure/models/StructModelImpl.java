package org.shrigorevich.ml.domain.structure.models;

public class StructModelImpl implements StructModel {
    public int id;
    public String world;
    public int typeId, x1, y1, z1, x2, y2, z2;

    public int volumeId;
    public String name;
    public int blocks, brokenBlocks, priority;
    public int deposit, resources;

    public StructModelImpl() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getWorld() {
        return world;
    }
    public void setWorld(String world) {
        this.world = world;
    }
    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public int getX1() {
        return x1;
    }
    public int getY1() {
        return y1;
    }
    public int getZ1() {
        return z1;
    }
    public int getX2() {
        return x2;
    }
    public int getY2() {
        return y2;
    }
    public int getZ2() {
        return z2;
    }
    public void setX1(int x1) {
        this.x1 = x1;
    }
    public void setY1(int y1) {
        this.y1 = y1;
    }
    public void setZ1(int z1) {
        this.z1 = z1;
    }
    public void setX2(int x2) {
        this.x2 = x2;
    }
    public void setY2(int y2) {
        this.y2 = y2;
    }
    public void setZ2(int z2) {
        this.z2 = z2;
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
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getDeposit() {
        return deposit;
    }
    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
    public int getResources() {
        return resources;
    }
    public void setResources(int resources) {
        this.resources = resources;
    }
}
