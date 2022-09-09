package org.shrigorevich.ml.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.project.ProjectService;

public class EntityDeathHandler implements Listener {

    private final NpcService npcService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final StructureService structService;

    public EntityDeathHandler(NpcService npcService, ProjectService projectService, TaskService taskService, StructureService structService) {
        this.npcService = npcService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.structService = structService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            npcService.getById(entity.getUniqueId()).ifPresent(npc -> {
                clearNpcData(npc);
//                if (villageService.getDeposit() >= 30) { //TODO: get from config
//                    npcService.load(npc.getId());
//                    villageService.updateDeposit(-30);
//                } else {
//                    System.out.println("Not enough gold");
//                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntityDeath(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.setCancelled(true);
            } else {
                System.out.println("Damage cause:" + event.getCause());
            }
        }
    }

    private void clearNpcData(StructNpc npc) {
        structService.getById(npc.getStructId()).ifPresent(s -> s.setLaborer(null));
        taskService.clear(npc.getEntityId());
        npcService.remove(npc.getEntityId());
    };

    private void requestNpc(StructNpc npc) {

    }
}
