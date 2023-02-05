package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void setup();
    void addMob(Entity entity, int power);
    void remove(Entity entity);
    int getCurrentQuantity();
    int getCurrentPower();
    List<EntityType> getMobTypesForRegSpawn();

}
