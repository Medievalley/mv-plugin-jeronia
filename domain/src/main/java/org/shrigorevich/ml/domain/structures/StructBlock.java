package org.shrigorevich.ml.domain.structures;

public interface StructBlock {
    int getId();
    int getX();
    int getY();
    int getZ();
    int getStructId();
    boolean isBroken();
    boolean isHealthPoint();
    BlockType getType();
    String getBlockData();
}
