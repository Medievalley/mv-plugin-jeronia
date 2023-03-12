package org.shrigorevich.ml.state.npc.models;

public interface StructNpcModel {

    int getId();
    int getSpawnX();
    int getSpawnY();
    int getSpawnZ();
    int getWorkX();
    int getWorkY();
    int getWorkZ();
    String getName();
    int getStructId();
    String getWorld();

    void setWorkX(int x);
    void setWorkY(int y);
    void setWorkZ(int z);
    void setSpawnX(int x);
    void setSpawnY(int y);
    void setSpawnZ(int z);
    void setName(String name);
    void setStructId(int structId);
    void setWorld(String world);
    void setAlive(boolean alive);
    boolean isAlive();
    void setRoleId(int id);
    int getRoleId();
}
