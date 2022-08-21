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
    void register(StructNpc npc);
    Optional<StructNpc> getById(UUID id);
    Optional<SafeLoc> bookSafeLoc(UUID entityId);
    void releaseSafeLoc(UUID entityId);
    void regSafeLoc(SafeLoc location);
}
