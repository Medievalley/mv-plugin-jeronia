package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
                block.getDrops().clear();
                block.breakNaturally(true);
                block.setType(initType);
            }

            finalizeHarvesting(event.getEntity().getUniqueId());
        }
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

    private void finalizeHarvesting(UUID entityId) {
        taskService.finalizeCurrent(entityId);

        npcService.getById(entityId).flatMap(npc ->
                structService.getById(npc.getStructId())).ifPresent(loreStructure ->
                loreStructure.updateFoodStock(1)); //TODO: get from config
    }
}
