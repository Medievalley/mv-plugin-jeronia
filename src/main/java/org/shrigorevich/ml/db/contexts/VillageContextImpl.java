package org.shrigorevich.ml.db.contexts;

import org.bukkit.plugin.Plugin;
import javax.sql.DataSource;

public class VillageContextImpl extends Context implements VillageContext {
    public VillageContextImpl(Plugin plugin, DataSource dataSource) {
        super(plugin, dataSource);
    }
}
