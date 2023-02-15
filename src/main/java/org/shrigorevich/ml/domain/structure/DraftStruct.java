package org.shrigorevich.ml.domain.structure;

import org.bukkit.Location;

public interface DraftStruct {
    String name();
    void name(String name);
    Location loc1();
    Location loc2();
    void loc1(Location l1);
    void loc2(Location l2);
    StructureType type();
    void type(StructureType type);
}
