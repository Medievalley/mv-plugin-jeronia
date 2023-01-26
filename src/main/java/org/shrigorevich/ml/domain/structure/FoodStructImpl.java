package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructBlock;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.WorkPlace;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodStructImpl extends TownInfraImpl implements FoodStructure {
    private final String name;
    private Villager laborer;
    private final int priority;
    private int foodStock;


    public FoodStructImpl(StructModel m, List<StructBlock> structBlocks) {
        super(m, structBlocks);
        this.name = m.getName();
        this.priority = m.getPriority();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getFoodStock() {
        return foodStock;
    }

    @Override
    public void updateFoodStock(int foodAmount) {
        foodStock+=foodAmount;
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


}
