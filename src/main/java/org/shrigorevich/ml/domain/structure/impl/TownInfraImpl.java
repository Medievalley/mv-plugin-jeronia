package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructModel;

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
