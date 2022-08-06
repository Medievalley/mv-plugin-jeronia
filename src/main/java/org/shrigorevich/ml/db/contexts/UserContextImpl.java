package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.users.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

public class UserContextImpl implements UserContext {

    private final Plugin plugin;
    private final DataSource dataSource;

    public UserContextImpl(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public Optional<User> getByName(String name) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<User> h = new BeanHandler<>(User.class);
            User user = run.query(String.format("SELECT u.id, u.username as name, u.ip, u.verified, ud.lives, r.name as rolename \n" +
                    "from users u JOIN player_data ud on u.id = ud.user_id JOIN role r on r.id = ud.role_id \n" +
                    "WHERE u.username = '%s'", name), h);

            if (user != null) {
                System.out.printf("%s, %s, %s, %s, %s%n", user.getName(), user.getIp(), user.getLives(), user.getRoleName(), user.isVerified());
            }
            return user == null ? Optional.empty() : Optional.of(user);

        } catch (SQLException ex) {
            plugin.getLogger().severe(ex.toString());
            return Optional.empty();
        }
    }
}
