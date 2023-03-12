package org.shrigorevich.ml.domain.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.structures.DraftStruct;
import org.shrigorevich.ml.domain.structures.StructureType;

import java.util.ArrayList;

class DraftStructImpl implements DraftStruct {

    private String name;
    private StructureType type;
    private final ArrayList<Location> locations;

    public DraftStructImpl() {
        this.locations = new ArrayList<>(2);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void name(String name) {
        this.name = name;
    }

    @Override
    public StructureType type() {
        return type;
    }

    @Override
    public void type(StructureType type) {
        this.type = type;
    }

    @Override
    public void addLocation(Location loc) {
        if (isLocated())
            locations.remove(0);
        locations.add(loc);
    }

    @Override
    public Location getFirstLoc() {
        return locations.size() > 0 ? locations.get(0) : null;
    }

    @Override
    public Location getSecondLoc() {
        return locations.size() > 1 ? locations.get(1) : null;
    }

    @Override
    public boolean isLocated() {
        return locations.size() == 2;
    }
}
