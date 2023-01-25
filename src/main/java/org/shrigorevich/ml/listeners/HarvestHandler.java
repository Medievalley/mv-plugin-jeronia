package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.HarvestTask;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.events.HarvestStartedEvent;
import org.shrigorevich.ml.events.StructPlantGrownEvent;
import org.shrigorevich.ml.events.UnableToHarvestEvent;

import java.util.UUID;

public class HarvestHandler implements Listener {

    private final TaskService taskService;
    private final NpcService npcService;
    private final StructureService structService;
    public HarvestHandler(TaskService taskService, NpcService npcService, StructureService structService) {
        this.taskService = taskService;
        this.npcService = npcService;
        this.structService = structService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlantGrown(StructPlantGrownEvent event) {
        Block b = event.getBlock();
        Villager entity = event.getEntity();
        taskService.add(
            new HarvestTask(
                taskService.getPlugin(),
                entity, b.getLocation()
            )
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnHarvest(HarvestStartedEvent event) {
        Block block = event.getTarget().getBlock();
        Material initType = block.getType();

        if (isPlant(initType)) {
            block.breakNaturally(true);
            block.setType(initType);

            Bukkit.getScheduler().runTaskAsynchronously(taskService.getPlugin(), () ->
                    updateStock(event.getEntity().getUniqueId()));
        }

        taskService.finalize(event.getEntity().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnUnableToHarvest(UnableToHarvestEvent event) {
        taskService.block(event.getEntity().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnItemSpawn(ItemSpawnEvent event) {
        if (isPlantFood(event.getEntity().getItemStack().getType())) {
            if (event.getEntity().getThrower() == null) {
                event.setCancelled(true);
            }
        }
    }

    //TODO: throw exception
    private void updateStock(UUID entityId) {
        npcService.getById(entityId).flatMap(npc ->
            structService.getStruct(npc.getStructId())).ifPresent(struct -> {
            if (struct instanceof FoodStructure fs) {
                fs.updateFoodStock(1); //TODO: get from config

            }
        });
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
