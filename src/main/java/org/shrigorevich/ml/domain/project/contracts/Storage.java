package org.shrigorevich.ml.domain.project.contracts;

public interface Storage {
    void updateDeposit(int amount);
    void updateResources(int amount);
    int getDeposit();
    int getResources();
}
