package org.shrigorevich.ml.domain.npc.models;

public class StructNpcModel implements StructNpcDB{

    private int x, y, z, structId, id;
    private String name;

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

    public void setId(int id) {
        this.id = id;
    }
}
