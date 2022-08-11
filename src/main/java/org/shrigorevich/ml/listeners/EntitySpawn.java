package org.shrigorevich.ml.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntitySpawnEvent;

import org.shrigorevich.ml.domain.goals.GoalService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.npc.StructNpcImpl;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;

import java.util.Optional;

@Deprecated
public class EntitySpawn implements Listener {
    private final GoalService goalService;
    private final NpcService npcService;
    private final StructureService structService;

    public EntitySpawn(GoalService goalService, NpcService npcService, StructureService structureService) {
        this.goalService = goalService;
        this.npcService = npcService;
        this.structService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {

    }
}
