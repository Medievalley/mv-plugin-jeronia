package org.shrigorevich.ml.domain.structure.impl;

import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.models.*;

import java.util.List;
import java.util.Optional;

class FoodStructImpl extends TownInfraImpl implements ExFoodStructure {

    private Villager laborer;
    private int resources;


    public FoodStructImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m, structBlocks);
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
}
