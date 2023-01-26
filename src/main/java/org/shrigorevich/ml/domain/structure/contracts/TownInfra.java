package org.shrigorevich.ml.domain.structure.contracts;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;

public interface TownInfra extends Structure {

    String getName();
    int getPriority();
    int getVolumeId();
    List<StructBlock> getStructBlocks();
    List<StructBlock> getBrokenBlocks();
}
