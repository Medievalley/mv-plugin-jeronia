package org.shrigorevich.ml.domain.mobs;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MobService {

    ItemStack getSkull(EntityType type);
    void setup();
    CustomMob createMob(Mob entity, MobType type, double power) throws IllegalArgumentException;
    void addMob(CustomMob mob);
    Optional<CustomMob> getMob(UUID id);
    void remove(UUID entityId);
    int getCurrentQuantity();
    int getCurrentPower();
    Map<EntityType, PresetUnit> getPressurePreset();
}
