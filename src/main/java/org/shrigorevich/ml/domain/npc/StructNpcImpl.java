package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.domain.npc.contracts.StructNpc;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.UUID;

public class StructNpcImpl implements StructNpc {

    private final int x, y, z, id, structId;
    private final String name, world;
    private final UUID entityId;
    private boolean alive;
    private NpcRole role;

    public StructNpcImpl(StructNpcModel model, UUID entityId) {
        this.x = model.getWorkX();
        this.y = model.getWorkY();
        this.z = model.getWorkZ();
        this.name = model.getName();
        this.world = model.getWorld();
        this.structId = model.getStructId();
        this.entityId = entityId;
        this.id = model.getId();
        this.alive = model.isAlive();
        this.role = parseRole(model.getRoleId());
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

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public NpcRole getRole() {
        return role;
    }

    private NpcRole parseRole(int roleId) {
        for(NpcRole role : NpcRole.values()) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        return null;
    }
}
