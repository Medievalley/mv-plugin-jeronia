package org.shrigorevich.ml.database.project;

import org.shrigorevich.ml.state.project.models.StorageModel;

public class StorageModelImpl implements StorageModel {
    public int deposit;
    public int resources;
    public int id;

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    @Override
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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
