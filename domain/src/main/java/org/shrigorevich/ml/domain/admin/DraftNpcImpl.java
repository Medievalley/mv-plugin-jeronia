package org.shrigorevich.ml.domain.admin;

import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.domain.npc.NpcRole;

public class DraftNpcImpl implements DraftNpc {
    private String name;
    private NpcRole role;
    private Coords spawnLoc;
    private Coords workLoc;
    private int structId;

    public DraftNpcImpl(Coords workLoc, int structId) {
        this.workLoc = workLoc;
        this.structId = structId;
    }

    public DraftNpcImpl(String name) {
        this.name = name;
    }

    public DraftNpcImpl(NpcRole role) {
        this.role = role;
    }

    public DraftNpcImpl(Coords spawnLoc) {
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
    public Coords getSpawnLoc() {
        return spawnLoc;
    }

    @Override
    public Coords getWorkLoc() {
        return workLoc;
    }

    @Override
    public void setSpawnLoc(Coords spawnLoc) {
        this.spawnLoc = spawnLoc;
    }

    @Override
    public void setWorkLoc(Coords workLoc) {
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
