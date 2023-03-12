package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.domain.structures.TownInfra;
import org.shrigorevich.ml.state.structures.models.StructModel;

import java.util.List;

abstract class TownInfraImpl extends VolumeStructImpl implements TownInfra {

    private final String name;
    private final int priority;


    public TownInfraImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m, structBlocks);
        this.priority = m.getPriority();
        this.name = m.getName();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getName() {
        return name;
    }
}
