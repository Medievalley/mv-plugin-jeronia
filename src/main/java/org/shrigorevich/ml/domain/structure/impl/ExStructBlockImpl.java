package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

class ExStructBlockImpl implements ExStructBlock {
    private final int id;
    private final int structId;
    private final int x, y, z;
    private boolean isBroken;
    private boolean isHealthPoint;
    private final String blockData;
    public ExStructBlockImpl(StructBlockModel m) {
        this.id = m.getId();
        this.structId = m.getStructId();
        this.x = m.getX();
        this.y = m.getY();
        this.z = m.getZ();
        this.isBroken = m.isBroken();
        this.isHealthPoint = m.isTriggerDestruction();
        this.blockData = m.getBlockData();
    }

    @Override
    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    @Override
    public void setIsHealthPoint(boolean isHealthPoint) {
        this.isHealthPoint = isHealthPoint;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public int getStructId() {
        return structId;
    }

    @Override
    public boolean isBroken() {
        return isBroken;
    }

    @Override
    public boolean isHealthPoint() {
        return isHealthPoint;
    }

    public String getBlockData() {
        return blockData;
    }
}
