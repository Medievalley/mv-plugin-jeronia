package org.shrigorevich.ml.db.models;

public class BrokenBlock {
    private int id, volumeBlockId, structId;

    public BrokenBlock() {}
    public BrokenBlock(int volumeBlockId, int structId) {
        this.volumeBlockId = volumeBlockId;
        this.structId = structId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolumeBlockId() {
        return volumeBlockId;
    }

    public void setVolumeBlockId(int volumeBlockId) {
        this.volumeBlockId = volumeBlockId;
    }

    public int getStructId() {
        return structId;
    }

    public void setStructId(int structId) {
        this.structId = structId;
    }
}
