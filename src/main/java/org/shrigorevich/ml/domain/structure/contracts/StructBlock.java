package org.shrigorevich.ml.domain.structure.contracts;

public interface StructBlock {
    int getX();
    int getY();
    int getZ();
    int getStructId();
    boolean isBroken();
    boolean isHealthPoint();
}
