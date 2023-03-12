package org.shrigorevich.ml.database.npc;

import org.shrigorevich.ml.state.npc.models.StructNpcModel;

public class StructNpcModelImpl implements StructNpcModel {

    private int structId, id, roleId;
    private int spawnX, spawnY, spawnZ, workX, workY, workZ;
    private String name, world;
    private boolean alive;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getWorkX() {
        return this.workX;
    }

    @Override
    public int getWorkY() {
        return this.workY;
    }

    @Override
    public int getWorkZ() {
        return this.workZ;
    }
    @Override
    public int getSpawnX() {
        return this.spawnX;
    }

    @Override
    public int getSpawnY() {
        return this.spawnY;
    }

    @Override
    public int getSpawnZ() {
        return this.spawnZ;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getStructId() {
        return this.structId;
    }

    @Override
    public String getWorld() {
        return this.world;
    }

    @Override
    public void setWorkX(int x) {
        this.workX = x;
    }

    @Override
    public void setWorkY(int y) {
        this.workY = y;
    }

    @Override
    public void setWorkZ(int z) {
        this.workZ = z;
    }
    @Override
    public void setSpawnX(int x) {
        this.spawnX = x;
    }

    @Override
    public void setSpawnY(int y) {
        this.spawnY = y;
    }

    @Override
    public void setSpawnZ(int z) {
        this.spawnZ = z;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setStructId(int structId) {
        this.structId = structId;
    }

    @Override
    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public int getRoleId() {
        return roleId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
