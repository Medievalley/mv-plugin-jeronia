package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.project.models.StorageModel;
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
