package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.domain.npc.models.StructNpcDB;

import java.util.UUID;

public class StructNpcImpl implements StructNpc {

    private final int x, y, z, id, structId;
    private final String name, world;
    private final UUID entityId;

    public StructNpcImpl(StructNpcDB model, UUID entityId) {
        this.x = model.getX();
        this.y = model.getY();
        this.z = model.getZ();
        this.name = model.getName();
        this.world = model.getWorld();
        this.structId = model.getStructId();
        this.entityId = entityId;
        this.id = model.getId();
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public UUID getEntityId() {
        return entityId;
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
