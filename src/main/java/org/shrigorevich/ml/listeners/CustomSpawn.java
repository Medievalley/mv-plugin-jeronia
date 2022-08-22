package org.shrigorevich.ml.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.tasks.ReachLocationTask;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.events.CustomSpawnEvent;

import java.util.Optional;

public class CustomSpawn implements Listener {
    private final TaskService taskService;
    private final NpcService npcService;
    private final StructureService structService;

    public CustomSpawn(TaskService taskService, NpcService npcService, StructureService structureService) {
        this.taskService = taskService;
        this.npcService = npcService;
        this.structService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnCustomSpawn(CustomSpawnEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager entity = (Villager) event.getEntity();
            Optional<StructNpc> npc = npcService.getById(entity.getUniqueId());

            if (npc.isPresent()) {
                assignToStruct(npc.get(), entity);
                taskService.setDefaultAI(entity);
                setTask(npc.get(), entity);
                System.out.printf("Custom spawned: %d, %s, %s%n",
                        npc.get().getId(), npc.get().isAlive(), npc.get().getRole());
            }
        }
    }

    private void assignToStruct(StructNpc npc, Villager entity) {
        Optional<LoreStructure> struct = structService.getById(npc.getStructId());
        struct.ifPresent(loreStructure -> loreStructure.setLaborer(entity));
    }

    private void setTask(StructNpc npc, Mob entity) {
        Location location = new Location(entity.getWorld(), npc.getX(), npc.getY(), npc.getZ());
        taskService.add(
                new ReachLocationTask(
                        taskService.getPlugin(),
                        TaskType.HOLD_SPAWN,
                        TaskPriority.LOW,
                        entity, location
                )
        );
    }
}
