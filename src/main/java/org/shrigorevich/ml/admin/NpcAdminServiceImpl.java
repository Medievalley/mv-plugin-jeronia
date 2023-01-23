package org.shrigorevich.ml.admin;

import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.CoordinatesImpl;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.contracts.DraftNpc;
import org.shrigorevich.ml.domain.npc.contracts.DraftNpcImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NpcAdminServiceImpl extends BaseService implements NpcAdminService {

    private final Map<String, DraftNpc> draftNpc;

    public NpcAdminServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger("NpcAdminServiceImpl"));
        this.draftNpc = new HashMap<>();
    }

    public Optional<DraftNpc> getDraftNpc(String key) {
        if (!draftNpc.containsKey(key)) {
            return Optional.empty();
        } else {
            return Optional.of(draftNpc.get(key));
        }
    }

    @Override
    public void draftNpcSetWork(int x, int y, int z, int structId, String key, MsgCallback cb) {
        if (draftNpc.containsKey(key)) {
            DraftNpc npc = draftNpc.get(key);
            npc.setWorkLoc(new CoordinatesImpl(x, y, z));
            npc.setStructId(structId);
        } else {
            draftNpc.put(key, new DraftNpcImpl(new CoordinatesImpl(x, y, z), structId));
        }
        cb.result(String.format("Draft npc: %s", draftNpc.get(key).getString()));
    }

    @Override
    public void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb) {
        if (draftNpc.containsKey(key)) {
            draftNpc.get(key).setSpawnLoc(new CoordinatesImpl(x, y, z));
        } else {
            draftNpc.put(key, new DraftNpcImpl(new CoordinatesImpl(x, y, z)));
        }
        cb.result(String.format("Draft npc: %s", draftNpc.get(key).getString()));
    }

    @Override
    public void draftNpcSetName(String key, String name, MsgCallback cb) {
        if (draftNpc.containsKey(key)) {
            draftNpc.get(key).setName(name);
        } else {
            draftNpc.put(key, new DraftNpcImpl(name));
        }
        cb.result(String.format("Draft npc: %s", draftNpc.get(key).getString()));
    }

    @Override
    public void draftNpcSetRole(String key, NpcRole role, MsgCallback cb) {
        if (draftNpc.containsKey(key)) {
            draftNpc.get(key).setRole(role);
        } else {
            draftNpc.put(key, new DraftNpcImpl(role));
        }
        cb.result(String.format("Draft npc: %s", draftNpc.get(key).getString()));
    }

    @Override
    public boolean isNpcValid(DraftNpc npc) {
        return !npc.getName().isBlank() &&
                npc.getStructId() != 0 &&
                npc.getRole() != null &&
                npc.getSpawnLoc() != null &&
                npc.getWorkLoc() != null;
    }
}
