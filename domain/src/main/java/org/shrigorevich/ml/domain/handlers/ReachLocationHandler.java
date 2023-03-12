package org.shrigorevich.ml.domain.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.shrigorevich.ml.domain.ai.BuildTask;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.events.BuildStartedEvent;
import org.shrigorevich.ml.domain.events.LocationReachedEvent;
import org.shrigorevich.ml.domain.events.UnableToReachLocationEvent;

public class ReachLocationHandler implements Listener {
    private final Plugin plugin;

    public ReachLocationHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(LocationReachedEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();

        switch (event.getTask().getType()) {
            case BUILD ->
                    pm.callEvent(new BuildStartedEvent(event.getEntity(), event.getTarget(), (BuildTask) event.getTask()));
            case HOLD_SPAWN -> {

            }
            default -> {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnUnableToReachLocation(UnableToReachLocationEvent event) {

    }
}
