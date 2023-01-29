package org.shrigorevich.ml.domain.structure;

import java.util.List;

public interface AbodeStructure extends Structure {
    String getName();
    List<StructBlock> getSpawnBlocks();
}
