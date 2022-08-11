package org.shrigorevich.ml.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.shrigorevich.ml.domain.goals.GoalService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.npc.StructNpcImpl;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.events.CustomSpawnEvent;

import java.util.Optional;

public class CustomSpawn implements Listener {
    private final GoalService goalService;
    private final NpcService npcService;
    private final StructureService structService;

    public CustomSpawn(GoalService goalService, NpcService npcService, StructureService structureService) {
        this.goalService = goalService;
        this.npcService = npcService;
        this.structService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnCustomSpawn(CustomSpawnEvent event) {
        Entity entity = event.getEntity();
        StructNpc npc = registerStructNpc(entity, event.getNpcModel());
        assignToStruct(npc, entity);
        setGoal(npc, entity);
        System.out.printf("Setup custom entity. Id: %d%n", npc.getId());
    }

    private StructNpc registerStructNpc(Entity entity, StructNpcDB model) {
        StructNpc npc = new StructNpcImpl(model, entity.getUniqueId());
        npcService.register(npc);
        return npc;
    }

    private void assignToStruct(StructNpc npc, Entity entity) {
        Optional<LoreStructure> struct = structService.getById(npc.getStructId());
        struct.ifPresent(loreStructure -> loreStructure.setLaborer(entity));
    }

    private void setGoal(StructNpc npc, Entity entity) {
        Location location = new Location(entity.getWorld(), npc.getX(), npc.getY(), npc.getZ());
        goalService.setGoGoal((Villager) entity, location);
    }
}
