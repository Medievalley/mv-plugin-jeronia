package org.shrigorevich.ml.domain.project.contracts;

import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;


public interface BuildProject extends Comparable<BuildProject> {
    int getHealthPercent();
    int getBrokenSize();
    int getId();
    void addPlannedBlock(StructBlockModel block);
    void addPlannedBlocks(List<StructBlockModel> blocks);
    StructBlockModel getPlannedBlock();
    boolean isPlanEmpty();
    int getPriority();
    TownInfra getStruct();
    int getPlanSize();
    void restoreOne();
}
