package org.shrigorevich.ml.domain.structure.contracts;

public interface Storage {
    int getResources();
    int getDeposit();
    void updateResources(int amount);
    void updateDeposit(int amount);
}
