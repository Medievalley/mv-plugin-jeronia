package org.shrigorevich.ml.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.shrigorevich.ml.domain.ai.contracts.BuildTask;
import org.shrigorevich.ml.events.*;

public class ReachLocationHandler implements Listener {
    private final Plugin plugin;

    public ReachLocationHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(LocationReachedEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();

        switch (event.getTask().getType()) {
            case HARVEST:
                pm.callEvent(new HarvestStartedEvent(event.getEntity(), event.getTarget(), event.getTask()));
                break;
            case BUILD:
                pm.callEvent(new BuildStartedEvent(event.getEntity(), event.getTarget(), (BuildTask) event.getTask()));
                break;
            case HOLD_SPAWN:
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnUnableToReachLocation(UnableToReachLocationEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();

        switch (event.getTask().getType()) {
            case HARVEST:
                pm.callEvent(new UnableToHarvestEvent(event.getEntity(), event.getTarget(), event.getTask()));
                break;
            case HOLD_SPAWN:
            default:
                break;
        }
    }
}
