package org.shrigorevich.ml.common;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;

public abstract class BaseService implements Service {
    private final Plugin plugin;
    private final Logger logger;

    protected BaseService(Plugin plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
