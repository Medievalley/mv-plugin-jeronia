package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.common.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NpcService extends Service {

    void commitNpc(DraftNpc npc) throws Exception;
    void setup() throws Exception;
    void setup(int id);
    void unload();
    void remove(UUID entityId);
    void reload();
    void reloadByStruct(int structId);
    Optional<StructNpc> getById(UUID id);
    Optional<SafeLoc> bookSafeLoc(UUID entityId);
    void releaseSafeLoc(UUID entityId);
    void regSafeLoc(SafeLoc location);
    List<StructNpc> getNpcByRole(NpcRole role);
}
