package org.shrigorevich.ml.state.project;

public interface Storage {
    void updateDeposit(int amount);
    void updateResources(int amount);
    int getDeposit();
    int getResources();
    int getId();
}
