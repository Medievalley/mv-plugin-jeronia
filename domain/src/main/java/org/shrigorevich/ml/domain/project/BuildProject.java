package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.domain.structures.TownInfra;

import java.util.List;

public interface BuildProject extends Comparable<BuildProject> {
    int getHealthPercent();
    int getBrokenSize();
    int getId();
    void addPlannedBlock(StructBlock block);
    void addPlannedBlocks(List<StructBlock> blocks);
    StructBlock getPlannedBlock();
    boolean isPlanEmpty();
    int getPriority();
    TownInfra getStruct();
    int getPlanSize();
    void restoreOne();
}
