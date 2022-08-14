package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.events.CustomSpawnEvent;

public class EntitySpawn implements Listener {

    private final Plugin plugin;
    public EntitySpawn(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            plugin.getServer().getPluginManager().callEvent(new CustomSpawnEvent(event.getEntity()));
        }
    }
}
