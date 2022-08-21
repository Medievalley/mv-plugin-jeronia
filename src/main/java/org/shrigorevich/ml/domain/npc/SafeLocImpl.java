package org.shrigorevich.ml.domain.npc;

import org.bukkit.Location;

public class SafeLocImpl implements SafeLoc {
    private final Location location;
    private final int structId;

    public SafeLocImpl(Location location, int structId) {
        this.location = location;
        this.structId = structId;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getStructId() {
        return structId;
    }
}
