package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.contracts.Storage;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructModel;

public abstract class TownInfraImpl extends StructureImpl implements TownInfra {

    private final int priority;
    private final int volumeId;
    private final Storage storage;

    public TownInfraImpl(StructModel m) {
        super(m);
        this.priority = m.getPriority();
        this.volumeId = m.getVolumeId();
        this.storage = new StorageImpl(m.getResources(), m.getDeposit());
    }

    @Override
    public Storage getStorage(){
        return storage;
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
