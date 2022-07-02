package org.shrigorevich.ml.db;
import java.sql.*;
import java.util.Properties;

public class Database {

    Connection connection;

    public Database() {

    }

    public Connection getConnection() throws SQLException {
        if (this.connection != null) {
            return  this.connection;
        }

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user","shrigorevich");
        props.setProperty("password","strongpass");
        props.setProperty("ssl","false");
        this.connection = DriverManager.getConnection(url, props);
        return this.connection;
    }
}
