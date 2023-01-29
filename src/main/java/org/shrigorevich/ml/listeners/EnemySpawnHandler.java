package org.shrigorevich.ml.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;
import org.shrigorevich.ml.domain.mob.events.*;


public class EnemySpawnHandler implements Listener {

    private BukkitTask regularSpawnTask;
    private BukkitTask waveSpawnTask;
    private final Logger logger;
    private final MlConfiguration config;
    private final MlPlugin plugin;

    public EnemySpawnHandler(MlConfiguration config, MlPlugin plugin) {
        this.logger = LogManager.getLogger("EnemySpawnHandler");
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnRunRegularTimer(RunRegularSpawnTimerEvent event) {
        if (regularSpawnTask != null) {
            regularSpawnTask = plugin.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            }, 5, 5); //TODO: get from config
        } else {
            logger.info("Regular spawn task does not exist");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStopRegularTimer(StopRegularSpawnTimerEvent event) {
        if (regularSpawnTask != null) {
            plugin.getScheduler().cancelTask(regularSpawnTask.getTaskId());
        } else {
            logger.info("Regular spawn task does not exist");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnRunWaveTimer(RunWaveSpawnTimerEvent event) {
        //TODO: not implemented
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStopWaveTimer(StopWaveSpawnTimerEvent event) {
        //TODO: not implemented
    }
}
