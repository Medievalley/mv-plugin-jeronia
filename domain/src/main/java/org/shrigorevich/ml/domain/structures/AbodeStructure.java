package org.shrigorevich.ml.domain.structures;

import java.util.List;

public interface AbodeStructure extends VolumeStruct {
    String getName();
    List<StructBlock> getSpawnBlocks();
}
