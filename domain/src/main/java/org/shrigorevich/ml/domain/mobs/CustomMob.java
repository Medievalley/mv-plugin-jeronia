package org.shrigorevich.ml.domain.mobs;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.common.Coords;

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
    ScanBox getScanBox();
    ScanBox getScanBox(int offsetY, int radiusX, int radiusY, int radiusZ);
}
