package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

public class PointMemoryUnitImpl extends BaseMemoryUnit implements PointMemoryUnit {

    private final Location location;

    public PointMemoryUnitImpl(MemoryKey key, Location location) {
        super(key);
        this.location = location;
    }


    @Override
    public Location getLocation() {
        return location;
    }
}
