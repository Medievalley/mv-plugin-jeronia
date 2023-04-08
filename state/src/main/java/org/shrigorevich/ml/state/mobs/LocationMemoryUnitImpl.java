package org.shrigorevich.ml.state.mobs;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;

public class LocationMemoryUnitImpl implements LocationMemoryUnit {
    private final Location location;
    private boolean visited;
    public LocationMemoryUnitImpl(Location location) {
        this.location = location;
    }
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return locationEquals(((LocationMemoryUnitImpl) obj).getLocation());
    }

    @Override
    public boolean locationEquals(Location l) {
        return this.location.getBlockX() == l.getBlockX() &&
                this.location.getBlockY() == l.getBlockY() &&
                this.location.getBlockZ() == l.getBlockZ();
    }
}
