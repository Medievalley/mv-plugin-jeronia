package org.shrigorevich.ml.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.tasks.GoToLocationTask;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.events.StartHarvestEvent;

public class HarvestHandler implements Listener {
    private final TaskService taskService;

    public HarvestHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnJobStart(StartHarvestEvent event) {
        Block b = event.getBlock();
        Villager entity = event.getEntity();

        taskService.add(
            new GoToLocationTask(
                taskService.getPlugin(),
                TaskPriority.MIDDLE,
                entity, b.getLocation()
            )
        );
    }
}
