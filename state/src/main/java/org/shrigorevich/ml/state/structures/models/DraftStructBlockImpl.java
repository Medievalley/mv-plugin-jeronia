package org.shrigorevich.ml.state.structures.models;

public class DraftStructBlockImpl implements DraftStructBlock {

    private final int structId;
    private final int volumeBlockId;
    private final boolean healthPoint;
    public DraftStructBlockImpl(int structId, int volumeBlockId, boolean healthPoint) {
        this.structId = structId;
        this.volumeBlockId = volumeBlockId;
        this.healthPoint = healthPoint;
    }
    @Override
    public int getStructId() {
        return structId;
    }

    @Override
    public int getVolumeBlockId() {
        return volumeBlockId;
    }

    @Override
    public boolean isHealthPoint() {
        return healthPoint;
    }
}
