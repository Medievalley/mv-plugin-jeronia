package org.shrigorevich.ml.domain.village;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.BaseService;

import java.util.HashMap;

public class VillageServiceImpl extends BaseService implements VillageService {
    private Treasury treasury;

    public VillageServiceImpl(Plugin plugin) {
        super(plugin);
        this.treasury = new TreasuryImpl(50, new HashMap<>());
    }

    @Override
    public void supplyResource(Material material, int number) {
        treasury.addResource(material, number);
    }

    @Override
    public void updateDeposit(int number) {
        treasury.updateDeposit(number);
    }

    @Override
    public int getDeposit() {
        return treasury.getDeposit();
    }

    @Override
    public int getResource(Material material) {
        return treasury.getResource(material);
    }
}
