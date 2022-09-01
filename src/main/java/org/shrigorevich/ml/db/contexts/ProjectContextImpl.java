package org.shrigorevich.ml.db.contexts;

import org.bukkit.plugin.Plugin;
import javax.sql.DataSource;

public class ProjectContextImpl extends Context implements ProjectContext {
    public ProjectContextImpl(Plugin plugin, DataSource dataSource) {
        super(plugin, dataSource);
    }
}
