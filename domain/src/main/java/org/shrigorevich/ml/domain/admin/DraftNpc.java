package org.shrigorevich.ml.domain.admin;

import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.domain.npc.NpcRole;

public interface DraftNpc {

    String getName();
    int getStructId();
    NpcRole getRole();
    Coords getSpawnLoc();
    Coords getWorkLoc();
    void setSpawnLoc(Coords spawnLoc);
    void setWorkLoc(Coords workLoc);
    void setName(String name);
    void setRole(NpcRole role);
    void setStructId(int id);
    String getString();
}
