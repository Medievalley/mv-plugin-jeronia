package org.shrigorevich.ml.domain.structure.impl;

import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.models.*;

import java.util.List;
import java.util.Optional;

class FoodStructImpl extends TownInfraImpl implements ExFoodStructure {
    private final String name;
    private Villager laborer;
    private final int priority;
    private int resources;


    public FoodStructImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m, structBlocks);
        this.name = m.getName();
        this.priority = m.getPriority();
    }

    public String getName() {
        return this.name;
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
    public int getPriority() {
        return priority;
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
