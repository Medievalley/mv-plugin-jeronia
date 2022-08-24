package org.shrigorevich.ml.domain.village;

import org.bukkit.Material;


public interface Treasury {
    int getDeposit();
    void updateDeposit(int number);
    int getResource(Material material);
    void addResource(Material material, int number);
}
