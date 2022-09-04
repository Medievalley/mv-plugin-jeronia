package org.shrigorevich.ml.domain.project;

public interface Storage {
    void updateDeposit(int amount);
    void updateResources(int amount);
    int getDeposit();
    int getResources();
}
