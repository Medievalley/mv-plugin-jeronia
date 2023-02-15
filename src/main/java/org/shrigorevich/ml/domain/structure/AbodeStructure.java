package org.shrigorevich.ml.domain.structure;

import java.util.List;

public interface AbodeStructure extends VolumeStruct {
    String getName();
    List<StructBlock> getSpawnBlocks();
}
