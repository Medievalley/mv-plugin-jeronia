package org.shrigorevich.ml.domain.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.common.callback.MsgCallback;
import org.shrigorevich.ml.domain.npc.NpcRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NpcAdminServiceImpl implements NpcAdminService {

    private final Map<String, DraftNpc> draftNpc;
    private final Logger logger;
    private final Plugin plugin;

    public NpcAdminServiceImpl(Plugin plugin) {
        this.logger = LogManager.getLogger("NpcAdminServiceImpl");
        this.draftNpc = new HashMap<>();
        this.plugin = plugin;
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
            npc.setWorkLoc(new Coords(x, y, z));
            npc.setStructId(structId);
        } else {
            draftNpc.put(key, new DraftNpcImpl(new Coords(x, y, z), structId));
        }
        cb.result(String.format("Draft npc: %s", draftNpc.get(key).getString()));
    }

    @Override
    public void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb) {
        if (draftNpc.containsKey(key)) {
            draftNpc.get(key).setSpawnLoc(new Coords(x, y, z));
        } else {
            draftNpc.put(key, new DraftNpcImpl(new Coords(x, y, z)));
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

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
