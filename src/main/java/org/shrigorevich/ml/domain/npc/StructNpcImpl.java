package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.domain.npc.models.StructNpcDB;

import java.util.UUID;

public class StructNpcImpl implements StructNpc {

    private int x, y, z, structId;
    private String name, world;
    private UUID id;

    public StructNpcImpl(StructNpcDB model, UUID id) {
        this.x = model.getX();
        this.y = model.getY();
        this.z = model.getZ();
        this.name = model.getName();
        this.world = model.getWorld();
        this.structId = model.getStructId();
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStructId() {
        return structId;
    }

    @Override
    public String getWorld() {
        return world;
    }
}
