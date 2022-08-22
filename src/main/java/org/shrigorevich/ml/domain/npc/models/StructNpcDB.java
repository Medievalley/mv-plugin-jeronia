package org.shrigorevich.ml.domain.npc.models;

public interface StructNpcDB {

    int getId();
    int getX();
    int getY();
    int getZ();
    String getName();
    int getStructId();
    String getWorld();

    void setX(int x);
    void setY(int y);
    void setZ(int z);
    void setName(String name);
    void setStructId(int structId);
    void setWorld(String world);
    void setAlive(boolean alive);
    boolean isAlive();
    void setRoleId(int id);
    int getRoleId();
}
