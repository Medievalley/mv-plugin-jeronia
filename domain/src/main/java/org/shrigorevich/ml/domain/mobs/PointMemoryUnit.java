package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

public interface PointMemoryUnit extends MemoryUnit {
    Location getLocation();
    boolean visited();
    void setVisited(boolean visited);
}