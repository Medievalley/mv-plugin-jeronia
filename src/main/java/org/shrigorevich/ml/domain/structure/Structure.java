package org.shrigorevich.ml.domain.structure;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.shrigorevich.ml.domain.enums.StructureType;

import java.util.List;

public interface Structure{

    int getId();
    World getWorld();
    StructureType getType();
    int getX1();
    int getY1();
    int getZ1();
    int getX2();
    int getY2();
    int getZ2();
    int getSizeX();
    int getSizeY();
    int getSizeZ();
    List<Block> getBlocks();
    boolean contains(Location l);
}