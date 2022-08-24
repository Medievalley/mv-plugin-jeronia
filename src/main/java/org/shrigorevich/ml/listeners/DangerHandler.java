package org.shrigorevich.ml.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.Task;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.tasks.ReachLocationTask;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.events.DangerIsGoneEvent;
import org.shrigorevich.ml.events.NpcInDangerEvent;

import java.util.Optional;

public class DangerHandler implements Listener {
    private final TaskService taskService;
    private final NpcService npcService;

    public DangerHandler(TaskService taskService, NpcService npcService) {
        this.taskService = taskService;
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void NpcInDanger(NpcInDangerEvent event) {
        Mob entity = event.getEntity();

        Optional<Task> task = taskService.get(entity.getUniqueId());
        if (task.isPresent() && task.get().getType() != TaskType.GO_SAFE) {
            npcService.bookSafeLoc(entity.getUniqueId()).ifPresent(safeLoc -> {
                taskService.add(
                    new ReachLocationTask(
                        taskService.getPlugin(),
                        TaskType.GO_SAFE,
                        TaskPriority.HIGH,
                        entity, safeLoc.getLocation()
                    )
                );
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void NpcInDanger(DangerIsGoneEvent event) {
        Entity entity = event.getEntity();
        taskService.get(entity.getUniqueId()).ifPresent(task -> {
            if (task.getType() != TaskType.HOLD_SPAWN) {
                taskService.finalize(entity.getUniqueId());
                npcService.releaseSafeLoc(entity.getUniqueId());
            }
        });
    }
}
