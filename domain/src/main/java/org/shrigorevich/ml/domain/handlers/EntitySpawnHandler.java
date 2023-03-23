package org.shrigorevich.ml.domain.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.events.CustomSpawnEvent;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MobService;

public class EntitySpawnHandler implements Listener {

    private final Plugin plugin;
    private final MobService mobService;
    public EntitySpawnHandler(Plugin plugin, MobService mobService) {
        this.plugin = plugin;
        this.mobService = mobService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {

        }
    }
}
