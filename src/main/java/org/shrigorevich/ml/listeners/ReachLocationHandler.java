package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.shrigorevich.ml.events.HarvestEvent;
import org.shrigorevich.ml.events.LocationReachedEvent;
import org.shrigorevich.ml.events.UnableToHarvestEvent;
import org.shrigorevich.ml.events.UnableToReachLocationEvent;

public class ReachLocationHandler implements Listener {
    private final Plugin plugin;

    public ReachLocationHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(LocationReachedEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();

        switch (event.getTaskData().getType()) {
            case HARVEST:
                pm.callEvent(new HarvestEvent(event.getEntity(), event.getTarget(), event.getTaskData()));
                break;
            case HOLD_SPAWN:
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnUnableToReachLocation(UnableToReachLocationEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();

        switch (event.getTaskData().getType()) {
            case HARVEST:
                pm.callEvent(new UnableToHarvestEvent(event.getEntity(), event.getTarget(), event.getTaskData()));
                break;
            case HOLD_SPAWN:
            default:
                break;
        }
    }
}
