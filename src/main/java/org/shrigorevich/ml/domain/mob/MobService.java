package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.shrigorevich.ml.domain.mob.custom.MobType;

import java.util.Map;
import java.util.UUID;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void setup();
    void addMob(Entity entity, MobType type, double power);
    void remove(UUID entityId);
    int getCurrentQuantity();
    int getCurrentPower();
    Map<EntityType, PresetUnit> getPressurePreset();
}
