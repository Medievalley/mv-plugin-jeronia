package org.shrigorevich.ml.domain.project.models;

public class StorageModelImpl implements StorageModel {
    public int deposit;
    public int resources;

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    @Override
    public int getDeposit() {
        return deposit;
    }

    @Override
    public int getResources() {
        return resources;
    }
}
