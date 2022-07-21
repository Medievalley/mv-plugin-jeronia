package org.shrigorevich.ml.domain.models;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.shrigorevich.ml.domain.enums.StructureType;

import java.util.List;

public interface IStructure {

    int getId();
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

    List<Block> getBlocks();

    boolean contains(Location l);

    int getSizeX();
    int getSizeY();
    int getSizeZ();
}
