package org.shrigorevich.ml;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.shrigorevich.ml.db.ConnectionPool;
import org.shrigorevich.ml.db.Database;

import java.sql.SQLException;

public final class Ml extends JavaPlugin {
    ConnectionPool cp;
    Database db;
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println(ChatColor.AQUA + "START");

        try {
            cp = ConnectionPool.create("jdbc:postgresql://localhost:5432/postgres", "shrigorevich", "strongpass");
        } catch (SQLException ex) {
            System.out.println(ChatColor.RED + ex.toString());
        }
        db = new Database();
        System.out.println("OK");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("END");
    }
}
