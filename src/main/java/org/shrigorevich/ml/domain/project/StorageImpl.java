package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.project.models.StorageModel;

public class StorageImpl implements Storage {

    private int deposit, resources;
    public StorageImpl(int deposit, int resources) {
        this.resources = resources;
        this.deposit = deposit;
        System.out.printf("Deposit: %d, Resources: %d%n", deposit, resources);
    }

    public StorageImpl(StorageModel m) {
        this(m.getDeposit(), m.getResources());
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
