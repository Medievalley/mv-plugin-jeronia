package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.domain.Service;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;

import java.util.Optional;
import java.util.UUID;

public interface NpcService extends Service {

    void draftNpc(int x, int y, int z, int structId, String key, MsgCallback cb);
    void commitNpc(String name, String key) throws IllegalArgumentException;
    void load();
    void unload();
    void reload();
    void reload(int structId);
    Optional<StructNpc> getById(UUID id);
}
