package org.shrigorevich.ml.admin;

import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.npc.NpcRole;

public interface NpcAdminService extends Service {
    void draftNpc(int x, int y, int z, int structId, String key, MsgCallback cb);
    void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb);
    void draftNpcSetName(String key, String name, MsgCallback cb);
    void draftNpcSetRole(String key, NpcRole role, MsgCallback cb);
    void commitNpc(String key, MsgCallback cb) throws Exception;

}
