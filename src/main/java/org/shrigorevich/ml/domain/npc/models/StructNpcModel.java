package org.shrigorevich.ml.domain.npc.models;

public class StructNpcModel implements StructNpcDB{

    private int x, y, z, structId, id, roleId;
    private String name, world;
    private boolean alive;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
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
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
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
