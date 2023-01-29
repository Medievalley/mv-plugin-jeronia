package org.shrigorevich.ml.domain.structure.models;

import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;

public class StructBlockModelImpl extends VolumeBlockModelImpl implements StructBlockModel {
    private int id, structId, volumeBlockId;
    private boolean broken, triggerDestruction;
    private int typeId;

    public StructBlockModelImpl(int structId, int volumeBlockId, boolean triggerDestruction) {
        super();
        this.volumeBlockId = volumeBlockId;
        this.structId = structId;
        this.triggerDestruction = triggerDestruction;
    }
    public StructBlockModelImpl() {
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
    @Override
    public void setTriggerDestruction(boolean triggerDestruction) {
        this.triggerDestruction = triggerDestruction;
    }

    @Override
    public boolean isTriggerDestruction() {
        return triggerDestruction;
    }
    @Override
    public int getTypeId() {
        return typeId;
    }
    @Override
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
