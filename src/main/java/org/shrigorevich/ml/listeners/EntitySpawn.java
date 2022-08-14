package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structure.StructureService;

@Deprecated
public class EntitySpawn implements Listener {
    private final TaskService goalService;
    private final NpcService npcService;
    private final StructureService structService;

    public EntitySpawn(TaskService goalService, NpcService npcService, StructureService structureService) {
        this.goalService = goalService;
        this.npcService = npcService;
        this.structService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {

    }
}
