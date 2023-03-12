package org.shrigorevich.ml.domain.admin;

import org.shrigorevich.ml.common.callback.MsgCallback;
import org.shrigorevich.ml.domain.Service;
import org.shrigorevich.ml.domain.npc.NpcRole;

import java.util.Optional;

public interface NpcAdminService extends Service {

    Optional<DraftNpc> getDraftNpc(String key);
    void draftNpcSetWork(int x, int y, int z, int structId, String key, MsgCallback cb);
    void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb);
    void draftNpcSetName(String key, String name, MsgCallback cb);
    void draftNpcSetRole(String key, NpcRole role, MsgCallback cb);
    boolean isNpcValid(DraftNpc npc);

}
