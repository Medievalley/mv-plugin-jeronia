package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

public interface LocationMemoryUnit {
    Location getLocation();
    boolean locationEquals(Location l);
    boolean isVisited();
    void setVisited(boolean visited);
}
