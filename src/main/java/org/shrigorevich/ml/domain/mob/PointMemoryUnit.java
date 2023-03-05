package org.shrigorevich.ml.domain.mob;

import org.bukkit.Location;

public interface PointMemoryUnit extends MemoryUnit {
    Location getLocation();
    boolean visited();
    void setVisited(boolean visited);
}
