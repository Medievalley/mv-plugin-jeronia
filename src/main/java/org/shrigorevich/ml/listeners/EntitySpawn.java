package org.shrigorevich.ml.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.goals.AfkGoal;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.services.StructureService;

import java.util.Optional;

public class EntitySpawn implements Listener {
    private final NpcService npcService;

    public EntitySpawn(NpcService npcService) {
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {

    }
}
