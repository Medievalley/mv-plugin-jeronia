package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.structure.models.StructBlockDB;


public interface BuildProject {
    int getHealthPercent();
    int getBrokenSize();
    int getStructId();
    void addPlannedBlock(StructBlockDB block);
    StructBlockDB getPlannedBlock();
    boolean isPlanEmpty();
    void decrementBrokenSize();
}
