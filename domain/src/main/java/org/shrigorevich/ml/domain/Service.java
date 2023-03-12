package org.shrigorevich.ml.domain;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;

public interface Service {

    //TODO: need to remove method
    Plugin getPlugin();
    Logger getLogger();
}
