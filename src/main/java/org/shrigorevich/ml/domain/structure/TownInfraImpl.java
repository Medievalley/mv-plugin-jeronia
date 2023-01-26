package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.contracts.StructBlock;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.StructModel;

import java.util.List;

public abstract class TownInfraImpl extends StructureImpl implements TownInfra {

    private final int priority;
    private final int volumeId;
    private final List<StructBlock> structBlocks;

    public TownInfraImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m);
        this.priority = m.getPriority();
        this.volumeId = m.getVolumeId();
        this.structBlocks = structBlocks;
    }

    @Override
    public List<StructBlock> getStructBlocks() {
        return structBlocks;
    }

    @Override
    public List<StructBlock> getBrokenBlocks() {
        return structBlocks.stream().filter(StructBlock::isBroken).toList();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getVolumeId() {
        return volumeId;
    }
}
