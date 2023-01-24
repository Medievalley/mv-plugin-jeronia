package org.shrigorevich.ml.domain.npc;

import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.CoordinatesImpl;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.npc.contracts.*;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;
import org.shrigorevich.ml.domain.npc.models.StructNpcModelImpl;

import java.util.*;
import java.util.stream.Collectors;

public class NpcServiceImpl extends BaseService implements NpcService {
    private final Map<UUID, StructNpc> npcList;
    private final NpcContext context;
    private final Queue<SafeLoc> safeLocs;
    private final Map<UUID, SafeLoc> bookedSafeLocs;

    public NpcServiceImpl(NpcContext context, Plugin plugin) {
        super(plugin, LogManager.getLogger("NpcServiceImpl"));
        this.context = context;
        this.npcList = new HashMap<>();
        this.safeLocs = new ArrayDeque<>();
        this.bookedSafeLocs = new HashMap<>();
    }

    @Override
    public void commitNpc(DraftNpc npc) throws Exception {
        try {
            int id = context.save(npc);
            load(id);
        } catch (Exception ex) {
            throw new Exception(String.format("Error while committing npc: %s", npc.toString()));
        }
    }

    public void load() {
        try {
            List<StructNpcModel> models = context.get();
            for (StructNpcModel m : models) {
                if (m.isAlive()) {
                    spawn(m);
                } else {
                    getLogger().info("Dead NPC was not spawned. Id: " + m.getId());
                }
            }
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    public void load(int id) {
        try {
            Optional<StructNpcModel> npc = context.get(id);
            npc.ifPresent(this::spawn);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
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
        List<Entity> villagers = entities.stream().filter(e -> e.getType() == EntityType.VILLAGER).toList();
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
        try {
            List<StructNpc> structNpcList = npcList.values().stream().filter(n -> n.getStructId() == structId).toList();
            for (StructNpc npc : structNpcList) {
                Entity e = Bukkit.getEntity(npc.getEntityId());
                if (e != null) {
                    e.remove();
                }
                npcList.remove(npc.getEntityId());
                load(npc.getId());
            }

            if (structNpcList.size() == 0) {
                List<StructNpcModel> models = context.getByStructId(structId);
                for (StructNpcModel m : models) {
                    spawn(m);
                }
            }
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
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

    @Override
    public List<StructNpc> getNpcByRole(NpcRole role) {
        return npcList.values().stream().filter(n -> n.getRole() == role).collect(Collectors.toList());
    }

    private void spawn(StructNpcModel model) {
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

    private void register(StructNpc npc) {
        npcList.put(npc.getEntityId(), npc);
    }
}
