package org.shrigorevich.ml.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.shrigorevich.ml.config.Configuration;
import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceCreator {

    public static DataSource createDataSource(Configuration pluginConfig) {

        Properties hikariProps = new Properties();

        hikariProps.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        hikariProps.setProperty("dataSource.serverName", pluginConfig.getDatabase().getHost());
        hikariProps.setProperty("dataSource.portNumber", String.valueOf(pluginConfig.getDatabase().getPort()));
        hikariProps.setProperty("dataSource.user", pluginConfig.getDatabase().getUser());
        hikariProps.setProperty("dataSource.password", pluginConfig.getDatabase().getPassword());
        hikariProps.setProperty("dataSource.databaseName", pluginConfig.getDatabase().getDatabase());

        HikariConfig hikariConfig = new HikariConfig(hikariProps);
        hikariConfig.setMaximumPoolSize(pluginConfig.getDatabase().getPoolSize());
        System.out.println("Create dataSource");
        return new HikariDataSource(hikariConfig);
    }
}
