package org.shrigorevich.ml;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public interface MlPlugin extends AdventurePlugin, Plugin {
    BukkitScheduler getScheduler();
    void callEvent(Event event);
}
