package org.shrigorevich.ml.domain.models;
import org.bukkit.World;
import org.shrigorevich.ml.domain.enums.StructureType;

public interface IStructure {

    String getName();
    String getOwner();
    World getWorld();
    boolean isDestructible();
    StructureType getType();
    int getX1();
    int getY1();
    int getZ1();
    int getX2();
    int getY2();
    int getZ2();
}
