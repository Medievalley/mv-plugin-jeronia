package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;


public interface BuildProject extends Comparable<BuildProject> {
    int getHealthPercent();
    int getBrokenSize();
    int getId();
    void addPlannedBlock(StructBlockModel block);
    StructBlockModel getPlannedBlock();
    boolean isPlanEmpty();
    void decrementBrokenSize();
    int getPriority();
    LoreStructure getStruct();
}
