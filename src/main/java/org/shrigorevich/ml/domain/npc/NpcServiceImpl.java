package org.shrigorevich.ml.domain.npc;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.contexts.NpcContext;
import org.shrigorevich.ml.domain.BaseService;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.goals.AfkGoal;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.*;
import java.util.stream.Collectors;

public class NpcServiceImpl extends BaseService implements NpcService {

    private final HashMap<String, StructNpcDB> draftNpc;
    private final HashMap<UUID, StructNpc> npcList;
    private final NpcContext context;

    public NpcServiceImpl(NpcContext context, Plugin plugin) {
        super(plugin);
        this.context = context;
        this.draftNpc = new HashMap<>();
        this.npcList = new HashMap<>();
    }

    @Override
    public void draftNpc(int x, int y, int z, int structId, String key, MsgCallback cb) {
        StructNpcDB npc = new StructNpcModel();
        npc.setX(x);
        npc.setY(y);
        npc.setZ(z);
        npc.setStructId(structId);
        draftNpc.put(key, npc);
        cb.onDraft(String.format("Draft npc created. x: %d, y: %d, z: %d, Struct: %d",
                npc.getX(), npc.getY(), npc.getZ(), npc.getStructId()));
    }

    @Override
    public void commitNpc(String name, String key) throws IllegalArgumentException {
        StructNpcDB npc = draftNpc.get(key);
        if (npc != null) {
            npc.setName(name);
            int id = context.save(npc);
            load(id);
        } else {
            throw new IllegalArgumentException("Please specify npc spawn coordinates first");
        }
    }

    private void register(StructNpcDB model) {
        World w = Bukkit.getWorld(model.getWorld());
        if (w != null) {
            Location spawnLocation = new Location(w, model.getX(), model.getY(), model.getZ());
            Entity entity = w.spawnEntity(
                    spawnLocation,
                    EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.CUSTOM,
                    (e) -> {
                        e.customName(Component.text(model.getName()));
                        e.setCustomNameVisible(true);
                        e.setMetadata("id", new FixedMetadataValue(getPlugin(), model.getId()));
                    }
            );
            StructNpc npc = new StructNpcImpl(model, entity.getUniqueId());
            npcList.put(npc.getEntityId(), npc);

            Villager villager = (Villager) entity;
            villager.setAgeLock(true);
            //villager.setAI(false); //TODO: handle it
            AfkGoal goal = new AfkGoal(getPlugin(), villager, spawnLocation);
            if (!Bukkit.getMobGoals().hasGoal(villager, goal.getKey())) {
                Bukkit.getMobGoals().addGoal(villager, 3, goal);
            }

            System.out.printf("Npc loaded: %d%n", model.getId());
        }
    }

    public void load() {
        List<StructNpcDB> models = context.get();
        for (StructNpcDB m : models) {
            register(m);
        }
    }

    private void load(int id) {
        Optional<StructNpcDB> npc = context.get(id);
        npc.ifPresent(this::register);
    }

    /** Called when the plugin stops */
    @Override
    public void unload() {
        for (StructNpc npc : npcList.values()) {
            Entity e = Bukkit.getEntity(npc.getEntityId());
            if (e != null) e.remove();
            System.out.printf("Npc unloaded. Id: %d%n", e.getMetadata("id").get(0).asInt());
        }
        npcList.clear();
    }

    @Override
    public void reload() {
        List<Entity> entities = Bukkit.getWorld("world").getEntities(); //TODO: fix hardcoded value
        List<Entity> villagers = entities.stream().filter(e -> e.getType() == EntityType.VILLAGER).collect(Collectors.toList());
        for (Entity v : villagers) {
            if (v.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                v.remove();
                System.out.printf("Npc unloaded: %s%n", v.customName());
            }
        }
        npcList.clear();
        load();
    }

    @Override
    public void reload(int structId) {
        List<StructNpc> structNpcList = npcList.values().stream().filter(n -> n.getStructId() == structId).collect(Collectors.toList());
        for (StructNpc npc : structNpcList) {
            Entity e = Bukkit.getEntity(npc.getEntityId());
            if (e != null) {
                e.remove();
            }
            npcList.remove(npc.getEntityId());
            load(npc.getId());
        }

        if (structNpcList.size() == 0) {
            List<StructNpcDB> models = context.getByStructId(structId);
            for (StructNpcDB m : models) {
                register(m);
            }
        }
    }

    @Override
    public Optional<StructNpc> getById(UUID id) {
        StructNpc npc = npcList.get(id);
        return npc == null ? Optional.empty() : Optional.of(npc);
    }
}
