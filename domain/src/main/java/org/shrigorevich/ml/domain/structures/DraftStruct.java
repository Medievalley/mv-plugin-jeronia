package org.shrigorevich.ml.domain.structures;

import org.bukkit.Location;

public interface DraftStruct {
    String name();
    void name(String name);
    StructureType type();
    void type(StructureType type);
    void addLocation(Location loc);
    Location getFirstLoc();
    Location getSecondLoc();
    boolean isLocated();
}
