package org.shrigorevich.ml.domain.mobs;

import com.destroystokyo.paper.entity.Pathfinder;

import java.util.UUID;

public interface CustomMob extends MemoryHolder {
    UUID getId();
    double getPower();
    MobType getType();
    Pathfinder getPathfinder();
}
