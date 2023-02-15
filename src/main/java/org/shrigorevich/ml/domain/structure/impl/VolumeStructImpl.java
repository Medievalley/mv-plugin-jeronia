package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.VolumeStruct;
import org.shrigorevich.ml.domain.structure.models.StructModel;

import java.util.List;

abstract class VolumeStructImpl extends StructureImpl implements VolumeStruct {

    private final int volumeId;
    private final List<StructBlock> structBlocks;

    public VolumeStructImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m);
        this.volumeId = m.getVolumeId();
        this.structBlocks = structBlocks;
    }

    @Override
    public int getVolumeId() {
        return volumeId;
    }

    @Override
    public List<StructBlock> getStructBlocks() {
        return structBlocks;
    }

    @Override
    public List<StructBlock> getBrokenBlocks() {
        return structBlocks.stream().filter(StructBlock::isBroken).toList();
    }
}
