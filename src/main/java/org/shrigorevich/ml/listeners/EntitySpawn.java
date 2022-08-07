package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import org.shrigorevich.ml.domain.npc.NpcService;


public class EntitySpawn implements Listener {
    private final NpcService npcService;

    public EntitySpawn(NpcService npcService) {
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {

    }
}
