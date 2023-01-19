package org.shrigorevich.ml.common;


import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;

public abstract class BaseContext implements Context {

    private final Logger logger;
    private final DataSource dataSource;

    protected BaseContext(DataSource dataSource, Logger logger) {
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
