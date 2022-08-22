package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.shrigorevich.ml.domain.npc.NpcService;

public class EntityDeathHandler implements Listener {

    private final NpcService npcService;

    public EntityDeathHandler(NpcService npcService) {
        this.npcService = npcService;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {

        }
    }
}
