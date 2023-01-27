package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.Storage;

public class StorageImpl implements Storage {

    private int resources;
    private int deposit;

    public StorageImpl(int resources, int deposit) {

    }

    @Override
    public void updateDeposit(int amount) {
        deposit+=amount;
    }

    @Override
    public void updateResources(int amount) {
        resources+=amount;
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
