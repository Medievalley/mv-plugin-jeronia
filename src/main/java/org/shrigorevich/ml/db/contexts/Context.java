package org.shrigorevich.ml.db.contexts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.sql.DataSource;

public abstract class Context {

    private final Plugin plugin;
    private final DataSource dataSource;

    protected Context(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
