package org.shrigorevich.ml.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.shrigorevich.ml.common.config.MlConfiguration;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceCreator {

    public static DataSource createDataSource(MlConfiguration pluginConfig) {

        Properties hikariProps = new Properties();

        hikariProps.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        hikariProps.setProperty("dataSource.serverName", pluginConfig.getDb().getHost());
        hikariProps.setProperty("dataSource.portNumber", String.valueOf(pluginConfig.getDb().getPort()));
        hikariProps.setProperty("dataSource.user", pluginConfig.getDb().getUser());
        hikariProps.setProperty("dataSource.password", pluginConfig.getDb().getPassword());
        hikariProps.setProperty("dataSource.databaseName", pluginConfig.getDb().getDatabase());

        HikariConfig hikariConfig = new HikariConfig(hikariProps);
        hikariConfig.setMaximumPoolSize(pluginConfig.getDb().getPoolSize());
        return new HikariDataSource(hikariConfig);
    }
}
