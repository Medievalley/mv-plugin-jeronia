package org.shrigorevich.ml.db.models;

public class GetStructModel {
    private int id;

    private int volumeId;
    private String name;
    private String owner;
    private String world;
    private boolean destructible;
    private int typeId, x1, y1, z1, x2, y2, z2;

    public int getId() {
        return id;
    }
    public int getVolumeId() {
        return volumeId;
    }
    public String getName() {
        return name;
    }
    public String getOwner() {
        return owner;
    }
    public String getWorld() {
        return world;
    }
    public boolean isDestructible() {
        return destructible;
    }
    public int getTypeId() {
        return typeId;
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
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public void setWorld(String world) {
        this.world = world;
    }
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
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
    public void setId(int id) {
        this.id = id;
    }
    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }
    public void setName(String name) {
        this.name = name;
    }
}
