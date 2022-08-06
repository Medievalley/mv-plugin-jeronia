package org.shrigorevich.ml.domain.npc;

import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface NpcService {

    void draftNpc(int x, int y, int z, int structId, String key);
    List<Entity> getNpcList();
    Optional<Entity> getById(int id);
    void register(int id, Entity entity);
    void clear();
    int getSize();

    void commitNpc(String name, String key) throws IllegalArgumentException;
}
