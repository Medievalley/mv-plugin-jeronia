package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.BlockType;
import org.shrigorevich.ml.domain.structure.StructBlock;

interface ExStructBlock extends StructBlock {
    void setIsBroken(boolean isBroken);
    void setIsHealthPoint(boolean isHealthPoint);
    void setType(BlockType type);
}
