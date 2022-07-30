package org.shrigorevich.ml.domain.structure.models;

import org.bukkit.Material;

public interface VolumeBlockDB {
    int getId();
    void setId(int id);
    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getZ();

    void setZ(int z);

    Material getType();

    void setType(String type);

    String getBlockData();

    void setBlockData(String blockData);
}
