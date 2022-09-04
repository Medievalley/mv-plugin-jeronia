package org.shrigorevich.ml.domain.project;

import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.*;

public class BuildProjectImpl implements BuildProject {
    private final LoreStructure structure;
    private final int size;
    private int brokenSize;
    private final Queue<StructBlockModel> plannedBlocks;

    public BuildProjectImpl(LoreStructure structure, int size) {
        this.structure = structure;
        this.plannedBlocks = new LinkedList<>();
        this.size = size;
        this.brokenSize = 0;
    }

    @Override
    public int getHealthPercent() {
        return 100 - (size > 0 ? brokenSize * 100 / size : 0);
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
    public void decrementBrokenSize() {
        brokenSize-=1;
    }

    @Override
    public int getPriority() {
        return structure.getPriority();
    }

    @Override
    public LoreStructure getStruct() {
        return structure;
    }

    @Override
    public int compareTo(@NotNull BuildProject o) {
        return o.getPriority() - this.getPriority();
    }
}
