package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.PriorityQueue;


public interface BuildProject extends Comparable<BuildProject> {
    int getHealthPercent();
    int getBrokenSize();
    int getId();
    void addPlannedBlock(StructBlockModel block);
    StructBlockModel getPlannedBlock();
    boolean isPlanEmpty();
    int getPriority();
    LoreStructure getStruct();
    int getPlanSize();
    void restoreBlock(StructBlockModel block);
    PriorityQueue<StructBlockModel> getPlan();
    StructBlockModel getCurrent();
}
