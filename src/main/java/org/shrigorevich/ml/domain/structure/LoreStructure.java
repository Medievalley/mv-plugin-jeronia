package org.shrigorevich.ml.domain.structure;

public interface LoreStructure extends Structure, Nameable, Volumeable {

    long getDestructionPercent();
    void setDestroyedPercent(int destroyedPercent);
    int getFoodStock();
}
