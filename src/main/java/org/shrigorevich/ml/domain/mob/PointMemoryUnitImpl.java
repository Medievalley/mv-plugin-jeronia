package org.shrigorevich.ml.domain.mob;

import org.bukkit.Location;

public class PointMemoryUnitImpl extends BaseMemoryUnit implements PointMemoryUnit {

    private final Location location;
    private boolean visited;

    public PointMemoryUnitImpl(Location location) {
        super(MemoryKey.INTEREST_POINT);
        this.location = location;
    }


    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean visited() {
        return visited;
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
