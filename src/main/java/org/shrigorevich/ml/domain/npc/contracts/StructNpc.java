package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.domain.npc.NpcRole;

import java.util.UUID;

public interface StructNpc {
    UUID getEntityId();
    int getId();
    int getX();
    int getY();
    int getZ();
    String getName();
    int getStructId();
    String getWorld();
    boolean isAlive();
    void setAlive(boolean alive);
    NpcRole getRole();


}
