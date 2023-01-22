package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.domain.npc.NpcRole;

public interface DraftNpc {

    String getName();
    int getStructId();
    NpcRole getRole();
    Coordinates getSpawnLoc();
    Coordinates getWorkLoc();
    void setSpawnLoc(Coordinates workLoc);
    void setName(String name);
    void setRole(NpcRole role);

    String getString();
}
