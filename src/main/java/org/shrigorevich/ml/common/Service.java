package org.shrigorevich.ml.common;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;

public interface Service {

    Plugin getPlugin();
    Logger getLogger();
}
