package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.domain.npc.NpcRole;

public class DraftNpcImpl implements DraftNpc {
    private String name;
    private NpcRole role;
    private Coordinates spawnLoc;
    private Coordinates workLoc;
    private int structId;

    public DraftNpcImpl(Coordinates workLoc, int structId) {
        this.workLoc = workLoc;
        this.structId = structId;
    }

    public DraftNpcImpl(String name) {
        this.name = name;
    }

    public DraftNpcImpl(NpcRole role) {
        this.role = role;
    }

    public DraftNpcImpl(Coordinates spawnLoc) {
        this.spawnLoc = spawnLoc;
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
    public NpcRole getRole() {
        return role;
    }

    @Override
    public Coordinates getSpawnLoc() {
        return spawnLoc;
    }

    @Override
    public Coordinates getWorkLoc() {
        return workLoc;
    }

    @Override
    public void setSpawnLoc(Coordinates spawnLoc) {
        this.spawnLoc = spawnLoc;
    }

    @Override
    public void setWorkLoc(Coordinates workLoc) {
        this.workLoc = workLoc;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRole(NpcRole role) {
        this.role = role;
    }

    @Override
    public void setStructId(int id) {
        this.structId = id;
    }

    @Override
    public String getString() {
        return String.format("{name: %s, role: %s, struct: %d, spawn: %s, work: %s}",
            name, role.toString(), structId,
            spawnLoc == null ? "" : spawnLoc.getString(),
            workLoc == null ? "" : workLoc.getString());
    }
}
