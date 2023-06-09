package org.shrigorevich.ml.state.structures;

import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.state.structures.models.StructModel;

import java.util.List;
import java.util.Optional;

class MainStructureImpl extends TownInfraImpl implements ExMainStructure {

    private Villager laborer;
    private int resources;
    private int deposit;

    public MainStructureImpl(StructModel m, List<StructBlock> blocks) {
        super(m, blocks);
        this.resources = m.getResources();
        this.deposit = m.getDeposit();
    }

    @Override
    public Optional<Villager> getLaborer() {
        return laborer == null ? Optional.empty() : Optional.of(laborer);
    }

    @Override
    public void setLaborer(Villager e) {
        this.laborer = e;
    }

    @Override
    public int getResources() {
        return resources;
    }

    @Override
    public void updateResources(int amount) {
        this.resources+=amount;
    }

    @Override
    public int getDeposit() {
        return deposit;
    }

    @Override
    public void updateDeposit(int amount) {
        this.deposit+=amount;
    }
}
