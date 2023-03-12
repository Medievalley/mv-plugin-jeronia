package org.shrigorevich.ml.common.config;

public interface DatabaseConf {
    String getHost();

    int getPort();

    String getDatabase();

    int getPoolSize();

    String getUser();

    String getPassword();
}
