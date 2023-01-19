package org.shrigorevich.ml.common;

import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.util.logging.Logger;

public abstract class Context {

    private final Logger logger;
    private final DataSource dataSource;

    protected Context(DataSource dataSource, Logger logger) {
        this.logger = logger;
        this.dataSource = dataSource;

    }

    public Logger getLogger() {
        return logger;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
