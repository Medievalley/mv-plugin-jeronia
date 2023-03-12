package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.BlockType;
import org.shrigorevich.ml.domain.structures.StructBlock;

interface ExStructBlock extends StructBlock {
    void setIsBroken(boolean isBroken);
    void setIsHealthPoint(boolean isHealthPoint);
    void setType(BlockType type);
}
