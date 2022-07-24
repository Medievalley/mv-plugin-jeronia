package org.shrigorevich.ml.db.models;

public class StructBlock {
    private int id, structId, volumeBlockId;
    private boolean broken;

    public StructBlock(int structId, int volumeBlockId, boolean broken) {
        this.volumeBlockId = volumeBlockId;
        this.structId = structId;
        this.broken = broken;
    }

    public StructBlock() {}

    public int getStructId() {
        return structId;
    }

    public void setStructId(int structId) {
        this.structId = structId;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public int getVolumeBlockId() {
        return volumeBlockId;
    }

    public void setVolumeBlockId(int volumeBlockId) {
        this.volumeBlockId = volumeBlockId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
