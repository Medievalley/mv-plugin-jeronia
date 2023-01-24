package org.shrigorevich.ml.domain.project;

import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.project.contracts.BuildProject;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;

import java.util.*;

public class BuildProjectImpl implements BuildProject {
    private final TownInfra structure;
    private final int size;
    private int brokenSize;
    private final PriorityQueue<StructBlockModel> plannedBlocks;

    public BuildProjectImpl(TownInfra structure, int size) {
        this.structure = structure;
        this.plannedBlocks = new PriorityQueue<>(Comparator.comparingInt(VolumeBlockModel::getY));
        this.size = size;
        this.brokenSize = 0;
    }

    @Override
    public int getHealthPercent() {
        return 100 - (size > 0 ? (int) Math.ceil((double) brokenSize / size) : 0);
    }

    @Override
    public int getBrokenSize() {
        return brokenSize;
    }

    @Override
    public int getId() {
        return structure.getId();
    }

    @Override
    public void addPlannedBlocks(List<StructBlockModel> blocks) {
        for (StructBlockModel b : blocks) {
            addPlannedBlock(b);
        }
    }

    @Override
    public void addPlannedBlock(StructBlockModel block) {
        brokenSize+=1;
        plannedBlocks.add(block);
    }

    @Override
    public StructBlockModel getPlannedBlock() {
        return plannedBlocks.poll();
    }

    @Override
    public boolean isPlanEmpty() {
        return plannedBlocks.isEmpty();
    }

    @Override
    public int getPriority() {
        return structure.getPriority();
    }

    @Override
    public TownInfra getStruct() {
        return structure;
    }

    @Override
    public int getPlanSize() {
        return plannedBlocks.size();
    }

    @Override
    public void restoreOne() {
        decrementBrokenSize();
    }

    private void decrementBrokenSize() {
        brokenSize-=1;
    }
    @Override
    public int compareTo(@NotNull BuildProject o) {
        return o.getPriority() - this.getPriority();
    }
}
