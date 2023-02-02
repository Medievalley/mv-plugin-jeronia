package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void setup();

    void addMob(Entity entity);
    void remove(Entity entity);
    int getMobPower(EntityType type);
    int getCurrentQuantity();
    int getCurrentPower();
    List<EntityType> getMobTypesForRegSpawn();

}
