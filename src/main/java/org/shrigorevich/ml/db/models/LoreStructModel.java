package org.shrigorevich.ml.db.models;

public class LoreStructModel extends StructModel {

    public int id;
    public int volumeId;
    /** ID of base structure */
    public String name;
    public boolean destructible;
    public int destroyedPercent;
    public LoreStructModel() {
        super();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getDestroyedPercent() {
        return destroyedPercent;
    }
    public void setDestroyedPercent(int destroyedPercent) {
        this.destroyedPercent = destroyedPercent;
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
}
