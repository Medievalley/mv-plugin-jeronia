package org.shrigorevich.ml.domain.mobs;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

import java.util.UUID;

public interface CustomMob extends MemoryHolder {
    Mob getHandle();
    UUID getId();
    double getPower();
    MobType getType();
    Pathfinder getPathfinder();
    Location getLocation();
    void removeVanillaAI();
    void setupAI();
}
