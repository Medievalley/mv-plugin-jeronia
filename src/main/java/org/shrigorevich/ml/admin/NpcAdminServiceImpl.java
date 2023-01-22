package org.shrigorevich.ml.admin;

import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.CoordinatesImpl;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.contracts.DraftNpc;
import org.shrigorevich.ml.domain.npc.contracts.DraftNpcImpl;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.HashMap;
import java.util.Map;

public class NpcAdminServiceImpl extends BaseService implements NpcAdminService {

    private final Map<String, DraftNpc> draftNpc;

    public NpcAdminServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger("NpcAdminServiceImpl"));
        this.draftNpc = new HashMap<>();
    }

    @Override
    public void draftNpc(int x, int y, int z, int structId, String key, MsgCallback cb) {
        DraftNpc npc = new DraftNpcImpl(new CoordinatesImpl(x, y, z), structId);
        draftNpc.put(key, npc);
        cb.result(String.format("Draft npc created. Work loc: %d %d %d, Struct: %d", x, y, z, npc.getStructId()));
    }

    @Override
    public void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb) {
        DraftNpc npc = draftNpc.get(key);
        npc.setSpawnLoc(new CoordinatesImpl(x, y, z));
        cb.result(String.format("Draft npc spawn loc: %d %d %d", x, y, z));
    }

    @Override
    public void draftNpcSetName(String key, String name, MsgCallback cb) {
        DraftNpc npc = draftNpc.get(key);
        if (npc != null) {
            npc.setName(name);
        } else {
            cb.result("First create dra");
        }
    }

    @Override
    public void draftNpcSetRole(String key, NpcRole role, MsgCallback cb) {

    }

    @Override
    public void commitNpc(String key, MsgCallback cb) throws Exception {

    }

    @Override
    public void commitNpc(String name, NpcRole role, String key) throws Exception {
        StructNpcModel npc = draftNpc.get(key);
        if (npc != null) {
            npc.setName(name);
            npc.setRoleId(role.getRoleId());
            try {
                int id = context.save(npc);
                load(id);
            } catch (Exception ex) {
                throw new Exception(String.format("Error while committing npc { name: %s, role: %d}",
                        npc.getName(), npc.getRoleId()));
            }
        } else {
            throw new Exception("Please specify npc spawn coordinates first");
        }
    }
}
