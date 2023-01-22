package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.domain.npc.NpcRole;

public class DraftNpcImpl implements DraftNpc {
    private String name;
    private NpcRole role;
    private Coordinates spawnLoc;
    private final Coordinates workLoc;
    private final int structId;

    public DraftNpcImpl(Coordinates workLoc, int structId) {
        this.workLoc = workLoc;
        this.structId = structId;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRole(NpcRole role) {
        this.role = role;
    }

    @Override
    public String getString() {

    }
}
