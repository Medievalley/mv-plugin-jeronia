package org.shrigorevich.ml;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public interface MlPlugin extends AdventurePlugin, Plugin {
    BukkitScheduler getScheduler();
}
