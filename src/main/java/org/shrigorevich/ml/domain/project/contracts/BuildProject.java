package org.shrigorevich.ml.domain.project.contracts;

import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;


public interface BuildProject extends Comparable<BuildProject> {
    int getHealthPercent();
    int getBrokenSize();
    int getId();
    void addPlannedBlock(StructBlockModel block);
    StructBlockModel getPlannedBlock();
    boolean isPlanEmpty();
    int getPriority();
    FoodStructure getStruct();
    int getPlanSize();
    void restoreBlock(StructBlockModel block);
}
