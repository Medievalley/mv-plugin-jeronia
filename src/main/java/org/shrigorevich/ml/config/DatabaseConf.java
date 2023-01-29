package org.shrigorevich.ml.config;

public interface DatabaseConf {
    String getHost();

    int getPort();

    String getDatabase();

    int getPoolSize();

    String getUser();

    String getPassword();
}
