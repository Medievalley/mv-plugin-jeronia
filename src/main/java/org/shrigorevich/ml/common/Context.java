package org.shrigorevich.ml.common;

import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;

public interface Context {
    Logger getLogger();
    DataSource getDataSource();
}
