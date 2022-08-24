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
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.*;
import java.util.stream.Collectors;

public class NpcServiceImpl extends BaseService implements NpcService {

    private final Map<String, StructNpcDB> draftNpc;
    private final Map<UUID, StructNpc> npcList;
    private final NpcContext context;
    private final Queue<SafeLoc> safeLocs;
    private final Map<UUID, SafeLoc> bookedSafeLocs;

    public NpcServiceImpl(NpcContext context, Plugin plugin) {
        super(plugin);
        this.context = context;
        this.draftNpc = new HashMap<>();
        this.npcList = new HashMap<>();
        this.safeLocs = new ArrayDeque<>();
        this.bookedSafeLocs = new HashMap<>();
    }

    @Override
    public void draftNpc(int x, int y, int z, int structId, String key, MsgCallback cb) {
        StructNpcDB npc = new StructNpcModel();
        npc.setWorkX(x);
        npc.setWorkY(y);
        npc.setWorkZ(z);
        npc.setStructId(structId);
        draftNpc.put(key, npc);
        cb.onDraft(String.format("Draft npc created. Work loc: %d %d %d, Struct: %d",
                npc.getWorkX(), npc.getWorkY(), npc.getWorkZ(), npc.getStructId()));
    }

    @Override
    public void draftNpcSetSpawn(int x, int y, int z, String key, MsgCallback cb) {
        StructNpcDB npc = draftNpc.get(key);
        npc.setSpawnX(x);
        npc.setSpawnY(y);
        npc.setSpawnZ(z);
        cb.onDraft(String.format("Draft npc spawn loc: %d %d %d",
                npc.getSpawnX(), npc.getSpawnY(), npc.getSpawnZ()));
    }

    @Override
    public void commitNpc(String name, NpcRole role, String key) throws IllegalArgumentException {
        StructNpcDB npc = draftNpc.get(key);
        if (npc != null) {
            npc.setName(name);
            npc.setRoleId(role.getRoleId());
            int id = context.save(npc);
            load(id);
        } else {
            throw new IllegalArgumentException("Please specify npc spawn coordinates first");
        }
    }

    private void spawn(StructNpcDB model) {
        World w = Bukkit.getWorld(model.getWorld());
        if (w != null) {
            Location spawnLocation = new Location(w, model.getSpawnX(), model.getSpawnY(), model.getSpawnZ());
            w.spawnEntity(
                spawnLocation,
                EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.CUSTOM,
                (e) -> {
                    e.customName(Component.text(model.getName()));
                    e.setCustomNameVisible(true);
                    e.setMetadata("id", new FixedMetadataValue(getPlugin(), model.getId()));
                    ((Villager) e).setAgeLock(true);
                    register(new StructNpcImpl(model, e.getUniqueId()));
                }
            );
        }
    }

    public void load() {
        List<StructNpcDB> models = context.get();
        for (StructNpcDB m : models) {
            if (m.isAlive()) {
                spawn(m);
            } else {
                System.out.println("Dead NPC was not spawned. Id: " + m.getId());
            }
        }
    }

    public void load(int id) {
        Optional<StructNpcDB> npc = context.get(id);
        npc.ifPresent(this::spawn);
    }

    /** Called when the plugin stops */
    @Override
    public void unload() {
        List<Entity> entities = Bukkit.getWorld("world").getEntities(); //TODO: fix hardcoded value
        for (Entity v : entities) {
            if (v.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                v.remove();
            }
        }
        npcList.clear();
    }

    @Override
    public void remove(UUID entityId) {
        npcList.remove(entityId);
    }

    @Override
    public void reload() {
        List<Entity> entities = Bukkit.getWorld("world").getEntities(); //TODO: fix hardcoded value
        List<Entity> villagers = entities.stream().filter(e -> e.getType() == EntityType.VILLAGER).collect(Collectors.toList());
        for (Entity v : villagers) {
            if (v.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                v.remove();
            }
        }
        npcList.clear();
        load();
    }

    @Override
    public void reloadByStruct(int structId) {
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
                spawn(m);
            }
        }
    }

    @Override
    public void register(StructNpc npc) {
        npcList.put(npc.getEntityId(), npc);
    }

    @Override
    public Optional<StructNpc> getById(UUID id) {
        StructNpc npc = npcList.get(id);
        return npc == null ? Optional.empty() : Optional.of(npc);
    }

    @Override
    public Optional<SafeLoc> bookSafeLoc(UUID entityId) {
        if (!bookedSafeLocs.containsKey(entityId)) {
            SafeLoc loc = safeLocs.poll();
            if (loc != null) {
                bookedSafeLocs.put(entityId, loc);
                System.out.println("Safe loc booked. Size: " + safeLocs.size());
                return Optional.of(loc);
            } else {
                System.out.println("No safe locations");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void releaseSafeLoc(UUID entityId) {
        SafeLoc loc = bookedSafeLocs.remove(entityId);
        if (loc != null) {
            safeLocs.add(loc);
        }
        System.out.println("Safe loc released. Size: " + safeLocs.size());
    }

    @Override
    public void regSafeLoc(SafeLoc location) {
        safeLocs.add(location);
        System.out.println("Safe loc registered. Size: " + safeLocs.size());
    }
}
