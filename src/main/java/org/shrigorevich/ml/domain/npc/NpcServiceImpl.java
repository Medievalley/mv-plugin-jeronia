package org.shrigorevich.ml.domain.npc;

import org.bukkit.entity.Entity;
import org.shrigorevich.ml.db.contexts.NpcContext;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.*;

public class NpcServiceImpl implements NpcService {

    private final HashMap<String, StructNpcDB> draftNpc;
    private final NpcContext context;

    public NpcServiceImpl(NpcContext context) {
        this.context = context;
        this.draftNpc = new HashMap<>();
    }

    @Override
    public void draftNpc(int x, int y, int z, int structId, String key) {
        StructNpcDB npc = new StructNpcModel();
        npc.setX(x);
        npc.setY(y);
        npc.setZ(z);
        npc.setStructId(structId);
        draftNpc.put(key, npc);
    }

    @Override
    public void commitNpc(String name, String key) throws IllegalArgumentException {
        StructNpcDB npc = draftNpc.get(key);
        if (npc != null) {
            npc.setName(name);
            context.save(npc);
        } else {
            throw new IllegalArgumentException("Please specify npc spawn coordinates first");
        }

    }

    @Override
    public List<Entity> getNpcList() {
        return null;
    }

    @Override
    public Optional<Entity> getById(int id) {
        return Optional.empty();
    }

    @Override
    public void register(int id, Entity entity) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }
}
