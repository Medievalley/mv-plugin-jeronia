package org.shrigorevich.ml.domain.structure;

import org.bukkit.Location;

import java.util.ArrayList;

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
