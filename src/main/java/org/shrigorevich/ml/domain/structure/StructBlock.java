package org.shrigorevich.ml.domain.structure;

public interface StructBlock {
    int getId();
    int getX();
    int getY();
    int getZ();
    int getStructId();
    boolean isBroken();
    boolean isHealthPoint();
    String getBlockData();
}
