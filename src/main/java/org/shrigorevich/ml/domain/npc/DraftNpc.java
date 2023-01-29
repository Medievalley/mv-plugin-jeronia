package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.domain.npc.NpcRole;

public interface DraftNpc {

    String getName();
    int getStructId();
    NpcRole getRole();
    Coordinates getSpawnLoc();
    Coordinates getWorkLoc();
    void setSpawnLoc(Coordinates spawnLoc);
    void setWorkLoc(Coordinates workLoc);
    void setName(String name);
    void setRole(NpcRole role);
    void setStructId(int id);
    String getString();
}
