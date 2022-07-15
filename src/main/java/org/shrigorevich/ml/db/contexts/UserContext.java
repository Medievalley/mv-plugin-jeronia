package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.shrigorevich.ml.domain.callbacks.IFindOneCallback;
import org.shrigorevich.ml.domain.models.User;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserContext implements IUserContext {

    private final Plugin plugin;
    private final DataSource dataSource;

    public UserContext(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public void getByNameAsync(String name, IFindOneCallback<User> callback) {

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {

            try {
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<User> h = new BeanHandler<>(User.class);
                User user = run.query(String.format("SELECT u.username as name, u.lastip, u.confirmed, ud.livesnumber, r.name as rolename \n" +
                        "from users u JOIN user_data ud on u.id = ud.userid JOIN roles r on r.id = ud.roleid \n" +
                        "WHERE u.username = '%s'", name), h);

                scheduler.runTask(plugin, () -> callback.onQueryDone(user));

            } catch (SQLException ex) {
                plugin.getLogger().severe(ex.toString());
            }
        });
    }

    public User getByName(String name) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<User> h = new BeanHandler<>(User.class);
            User user = run.query(String.format("SELECT u.username as name, u.lastip, u.confirmed, ud.livesnumber, r.name as rolename \n" +
                    "from users u JOIN user_data ud on u.id = ud.userid JOIN roles r on r.id = ud.roleid \n" +
                    "WHERE u.username = '%s'", name), h);

            if (user != null) {
                System.out.printf("%s, %s, %s, %s, %s%n", user.getName(), user.getLastIp(), user.getLivesNumber(), user.getRoleName(), user.isConfirmed());
            }
            return user;

        } catch (SQLException ex) {
            plugin.getLogger().severe(ex.toString());
        }
        return null;
    }
}
