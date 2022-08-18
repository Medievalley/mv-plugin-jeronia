package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.tasks.ReachLocationTask;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.events.LocationReachedEvent;
import org.shrigorevich.ml.events.StartHarvestEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HarvestHandler implements Listener {
    private final TaskService taskService;
    private final StructureService structService;
    private final NpcService npcService;

    public HarvestHandler(TaskService taskService, StructureService structureService, NpcService npcService) {
        this.taskService = taskService;
        this.structService = structureService;
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnJobStart(StartHarvestEvent event) {
        Block b = event.getBlock();
        Villager entity = event.getEntity();

        taskService.add(
            new ReachLocationTask(
                taskService.getPlugin(),
                TaskType.HARVEST,
                TaskPriority.MIDDLE,
                entity, b.getLocation()
            )
        );
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(LocationReachedEvent event) {

        if (event.getTaskData().getType() == TaskType.HARVEST) {
            Block block = event.getLocation().getBlock();
            Material initType = block.getType();

            if (isPlant(initType)) {
                block.breakNaturally(true);
                block.setType(initType);

                Bukkit.getScheduler().runTaskAsynchronously(taskService.getPlugin(), () ->
                        updateStock(event.getEntity().getUniqueId()));
            }

            taskService.finalizeCurrent(event.getEntity().getUniqueId());
            //TODO: dangerous (scheduled Bukkit api)

        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(EntityPickupItemEvent event) {

        npcService.getById(event.getEntity().getUniqueId()).ifPresent(npc -> {
            if (isPlantFood(event.getItem().getItemStack().getType())) {
                System.out.println("Pickup event canceled");
                event.setCancelled(true);
                event.getItem().remove();
            }
        });
    }
    private void updateStock(UUID entityId) {
        npcService.getById(entityId).flatMap(npc ->
                structService.getById(npc.getStructId())).ifPresent(loreStructure ->
                loreStructure.updateFoodStock(1)); //TODO: get from config
    }
    private boolean isPlant(Material type) {
        switch (type) {
            case WHEAT:
            case POTATOES:
            case CARROTS:
                return true;
            default:
                return false;
        }
    }

    private boolean isPlantFood(Material type) {
        switch (type) {
            case WHEAT:
            case POTATO:
            case CARROT:
                return true;
            default:
                return false;
        }
    }
}
