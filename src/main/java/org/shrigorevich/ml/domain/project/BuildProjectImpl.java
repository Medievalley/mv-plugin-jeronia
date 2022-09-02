package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.*;

public class BuildProjectImpl implements BuildProject {
    private final int structId;
    private final int size;
    private int brokenSize;
    private final Queue<StructBlockModel> plannedBlocks;

    public BuildProjectImpl(int structId, int size) {
        this.structId = structId;
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
    public int getStructId() {
        return structId;
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
}
