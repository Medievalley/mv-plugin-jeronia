package org.shrigorevich.ml.domain.users;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.users.contracts.UserContext;
import org.shrigorevich.ml.domain.users.models.UserModel;
import org.shrigorevich.ml.domain.users.models.UserModelImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

public class UserContextImpl extends BaseContext implements UserContext {

    public UserContextImpl(DataSource dataSource) {
        super(dataSource, Logger.getLogger("UserContextImpl"));
    }

    public Optional<UserModel> getByName(String name) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<UserModel> h = new BeanHandler<>(UserModelImpl.class);
            UserModel user = run.query(String.format("SELECT u.id, u.username as name, u.ip, u.verified, ud.lives, r.name as rolename \n" +
                    "from users u JOIN player_data ud on u.id = ud.user_id JOIN role r on r.id = ud.role_id \n" +
                    "WHERE u.username = '%s'", name), h);

            if (user != null) {
                getLogger().info(String.format("%s, %s, %s, %d, %s%n", user.getName(), user.getIp(), user.getLives(), user.getRoleId(), user.isVerified()));
            }
            return user == null ? Optional.empty() : Optional.of(user);

        } catch (SQLException ex) {
            getLogger().severe(ex.toString());
            throw new Exception(ex.toString());
        }
    }
}
