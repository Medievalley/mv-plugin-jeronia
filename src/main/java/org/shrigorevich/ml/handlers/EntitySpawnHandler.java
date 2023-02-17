package org.shrigorevich.ml.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.npc.events.CustomSpawnEvent;

public class EntitySpawnHandler implements Listener {

    private final Plugin plugin;
    public EntitySpawnHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            plugin.getServer().getPluginManager().callEvent(new CustomSpawnEvent(event.getEntity()));
        }
    }
}
