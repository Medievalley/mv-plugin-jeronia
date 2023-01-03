package org.shrigorevich.ml.common;

import org.bukkit.plugin.Plugin;

public abstract class BaseService {
    private final Plugin plugin;

    protected BaseService(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
