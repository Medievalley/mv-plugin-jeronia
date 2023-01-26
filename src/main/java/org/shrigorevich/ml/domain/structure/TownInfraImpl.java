package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.StructModel;

import java.util.List;

public abstract class TownInfraImpl extends StructureImpl implements TownInfra {

    private final int priority;
    private final int volumeId;
    private final List<StructBlockModel> structBlocks;

    public TownInfraImpl(StructModel m, List<StructBlockModel> structBlocks) {
        super(m);
        this.priority = m.getPriority();
        this.volumeId = m.getVolumeId();
        this.structBlocks = structBlocks;
    }

    @Override
    public List<StructBlockModel> getStructBlocks() {
        return structBlocks;
    }

    @Override
    public List<StructBlockModel> getBrokenBlocks() {
        return structBlocks.stream().filter(StructBlockModel::isBroken).toList();
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
