package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;
import org.shrigorevich.ml.domain.mob.events.SpawnRegularMobsEvent;
import org.shrigorevich.ml.domain.mob.events.SpawnWaveEvent;

public class SpawnEnemyHandler implements Listener {

    private MlConfiguration config;
    private MlPlugin plugin;

    public SpawnEnemyHandler(MlConfiguration config, MlPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler
    public void OnRegularSpawn(SpawnRegularMobsEvent event) {

    }

    @EventHandler
    public void OnWaveSpawn(SpawnWaveEvent event) {

    }

}
