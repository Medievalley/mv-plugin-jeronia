package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.tasks.ReachLocationTask;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.events.HarvestEvent;
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
            new ReachLocationTask(
                taskService.getPlugin(),
                TaskType.HARVEST,
                TaskPriority.MIDDLE,
                entity, b.getLocation()
            )
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnHarvest(HarvestEvent event) {
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
}
