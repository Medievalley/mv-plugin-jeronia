package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void setup();
    void addMob(Entity entity, double power);
    void remove(Entity entity);
    int getCurrentQuantity();
    int getCurrentPower();
    Map<EntityType, PresetUnit> getPressurePreset();
}
