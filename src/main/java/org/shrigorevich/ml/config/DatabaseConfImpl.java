package org.shrigorevich.ml.config;

import java.util.Map;

class DatabaseConfImpl implements DatabaseConf {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private final int poolSize;

    public DatabaseConfImpl(Map<String, Object> map) {
        host = (String) map.getOrDefault("host", "localhost");
        port = (int) map.getOrDefault("port", "5432");
        database = (String) map.getOrDefault("database", "postgres");
        user = (String) map.getOrDefault("user", "postgres");
        password = (String) map.getOrDefault("password", "password");
        poolSize = (int) map.getOrDefault("poolSize", "8");
    }

    public DatabaseConfImpl() {
        host = "localhost";
        port = 5432;
        database = "postgres";
        user = "postgres";
        password = "password";
        poolSize = 8;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}