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
    ValleyMob createMob(Mob entity, MobType type, double power) throws IllegalArgumentException;
    void addMob(ValleyMob mob);
    Optional<ValleyMob> getMob(UUID id);
    void remove(UUID entityId);
    int getCurrentQuantity();
    int getCurrentPower();
    Map<EntityType, PresetUnit> getPressurePreset();
}
