package org.shrigorevich.ml.domain.npc;

import java.util.UUID;

public interface StructNpc {
    UUID getId();
    int getX();
    int getY();
    int getZ();
    String getName();
    int getStructId();
    String getWorld();
}
