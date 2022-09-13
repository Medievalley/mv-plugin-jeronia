package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void load();
}
