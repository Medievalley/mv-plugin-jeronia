package org.shrigorevich.ml.config;

import org.bukkit.plugin.Plugin;

public interface MlConfiguration {

    void updateRegSpawnInterval(int value);
    DatabaseConf getDb();
    Plugin getPlugin();
}
