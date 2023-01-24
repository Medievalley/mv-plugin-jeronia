package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.contracts.Storage;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.StructModel;

import java.util.ArrayList;
import java.util.List;

public abstract class TownInfraImpl extends StructureImpl implements TownInfra {

    private final int priority;
    private final int volumeId;

    public TownInfraImpl(StructModel m) {
        super(m);
        this.priority = m.getPriority();
        this.volumeId = m.getVolumeId();
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
