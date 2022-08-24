package org.shrigorevich.ml.domain.village;

import org.bukkit.Material;

import java.util.Map;

public class TreasuryImpl implements Treasury {

    private int deposit;
    private final Map<Material, Integer> resources;

    public TreasuryImpl(int deposit, Map<Material, Integer> resources) {
        this.deposit = deposit;
        this.resources = resources;
    }

    @Override
    public int getDeposit() {
        return deposit;
    }

    @Override
    public void updateDeposit(int number) {
        deposit+=number;
        System.out.println("Deposit: " + deposit);
    }

    @Override
    public int getResource(Material material) {
        return resources.getOrDefault(material, 0);
    }

    @Override
    public void addResource(Material material, int number) {
        int curNumber = getResource(material) + number;
        resources.put(material, number);
    }
}
