package org.shrigorevich.ml.state.project;

import org.shrigorevich.ml.state.project.models.StorageModel;

class StorageImpl implements Storage {

    private int resources, deposit;
    private final int id;

    public StorageImpl(StorageModel m) {
        this.id = m.getId();
        this.resources = m.getResources();
        this.deposit = m.getDeposit();
    }

    @Override
    public void updateDeposit(int amount) {
        this.deposit+=amount;
    }

    @Override
    public void updateResources(int amount) {
        this.resources+=amount;
    }

    @Override
    public int getDeposit() {
        return deposit;
    }

    @Override
    public int getResources() {
        return resources;
    }

    @Override
    public int getId() {
        return id;
    }
}
