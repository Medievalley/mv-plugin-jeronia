package org.shrigorevich.ml.domain.village;

import org.bukkit.Material;
import org.shrigorevich.ml.domain.Service;

public interface VillageService extends Service {

    void supplyResource(Material material, int number);
    void updateDeposit(int number);
    int getDeposit();
    int getResource(Material material);
}
