package org.shrigorevich.ml.domain.mob;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Location;

import java.util.UUID;

public interface CustomMob extends MemoryHolder {
    UUID getId();
    double getPower();
    MobType getType();
    Pathfinder getPathfinder();
}
