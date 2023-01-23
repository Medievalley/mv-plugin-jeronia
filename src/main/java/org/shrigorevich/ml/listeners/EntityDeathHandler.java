package org.shrigorevich.ml.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.shrigorevich.ml.domain.ai.contracts.BuildTask;
import org.shrigorevich.ml.domain.ai.contracts.Task;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.contracts.StructNpc;
import org.shrigorevich.ml.domain.project.contracts.BuildProject;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.project.contracts.ProjectService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.*;

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
        LivingEntity entity = event.getEntity();
        if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (entity.getType() == EntityType.VILLAGER) {
                npcService.getById(entity.getUniqueId()).ifPresent(this::processNpcDeath);
            } else {
                //TODO: handle custom spawned mobs;
            }
        } else if (getReward(entity.getType()) > 0){
            ItemStack item = new ItemStack(Material.NETHER_STAR, getReward(entity.getType()));
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Jeron"));
            item.setItemMeta(meta);
            if (entity.getKiller() != null) {
                Map<Integer, ItemStack> change = entity.getKiller().getInventory().addItem(item);
                for (ItemStack itemStack : change.values()) {
                    entity.getWorld().dropItem(entity.getKiller().getLocation(), itemStack);
                }
            } else {
                entity.getWorld().dropItem(entity.getKiller().getLocation(), item);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntityDeath(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.setCancelled(true);
            } else {
//                System.out.println("Damage cause:" + event.getCause());
            }
        }
    }

    private void processNpcDeath(StructNpc npc) {
        switch (npc.getRole()) {
            case BUILDER:
                boolean isCurrentProject = false;
                Optional<BuildProject> current = projectService.getCurrent();
                List<Task> npcTasks = taskService.getEntityTasks(npc.getEntityId());
                for (Task task : npcTasks) {
                    if (task.getType() == TaskType.BUILD) {
                        StructBlockModel block = ((BuildTask) task).getBlock();
                        projectService.getProject(block.getStructId()).ifPresent(project -> {
                            project.addPlannedBlock(block);
                            projectService.updateResources(1);
                        });
                        if (current.isPresent() && current.get().getId() == block.getStructId()){
                            isCurrentProject = true;
                        }
                    }
                };
                if (isCurrentProject) {
                    System.out.println("Current project updated");
                }

                break;
            case HARVESTER:
                break;
            default:
                break;
        }
        clearNpcData(npc);
    }

    private void clearNpcData(StructNpc npc) {
        structService.getById(npc.getStructId()).ifPresent(s -> ((FoodStructure) s).setLaborer(null));
        taskService.clear(npc.getEntityId());
        npcService.remove(npc.getEntityId());
    };

    private int getReward(EntityType type) {
        return switch (type) {
            case ZOMBIE, SKELETON -> 1;
            case CREEPER -> 2;
            default -> 0;
        };
    }
}
