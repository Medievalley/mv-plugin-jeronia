package org.shrigorevich.ml.domain.structure.models;

public class StructBlockModel extends VolumeBlockModel implements StructBlockDB {
    private int id, structId, volumeBlockId;
    private boolean broken, triggerDestruction;

    public StructBlockModel(int structId, int volumeBlockId, boolean triggerDestruction) {
        super();
        this.volumeBlockId = volumeBlockId;
        this.structId = structId;
        this.triggerDestruction = triggerDestruction;
    }
    public StructBlockModel() {
        super();
    }
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

    @Override
    public void setTriggerDestruction(boolean triggerDestruction) {
        this.triggerDestruction = triggerDestruction;
    }

    @Override
    public boolean isTriggerDestruction() {
        return triggerDestruction;
    }
}
