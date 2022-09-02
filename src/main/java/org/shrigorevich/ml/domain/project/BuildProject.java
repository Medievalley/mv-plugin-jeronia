package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;


public interface BuildProject {
    int getHealthPercent();
    int getBrokenSize();
    int getStructId();
    void addPlannedBlock(StructBlockModel block);
    StructBlockModel getPlannedBlock();
    boolean isPlanEmpty();
    void decrementBrokenSize();
}
