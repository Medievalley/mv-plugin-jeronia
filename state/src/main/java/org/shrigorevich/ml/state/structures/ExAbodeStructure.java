package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.AbodeStructure;
import org.shrigorevich.ml.domain.structures.StructBlock;

public interface ExAbodeStructure extends AbodeStructure {
    void addSpawnBlock(StructBlock block);
}
