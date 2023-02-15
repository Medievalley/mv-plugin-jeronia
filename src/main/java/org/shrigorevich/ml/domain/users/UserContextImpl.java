package org.shrigorevich.ml.domain.users;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.users.contracts.UserContext;
import org.shrigorevich.ml.domain.users.models.UserModel;
import org.shrigorevich.ml.domain.users.models.UserModelImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

public class UserContextImpl extends BaseContext implements UserContext {

    private final UserQueryBuilder queryBuilder;
    public UserContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("UserContextImpl"));
        this.queryBuilder = new UserQueryBuilder();
    }

    public Optional<UserModel> getByName(String name) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<UserModel> h = new BeanHandler<>(UserModelImpl.class);
            UserModel user = run.query(queryBuilder.getByName(name), h);

            if (user != null) {
                getLogger().debug(String.format("%s, %s, %s, %d, %s%n", user.getName(), user.getIp(), user.getLives(), user.getRoleId(), user.isVerified()));
            }
            return user == null ? Optional.empty() : Optional.of(user);

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while getting user with name: %s", name));
        }
    }

    public void updateKillStatistics(String userId, String entityType) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(queryBuilder.updateKillStatistics(userId, entityType));
        } catch (SQLException ex){
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while updating kill statistics with userId: %s and entityType: %s",
                    userId, entityType));
        }
    }

    public void updateDeathStatistics(String userId, String deathReason) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(queryBuilder.updateDeathStatistics(userId, deathReason));
        } catch (SQLException ex){
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while updating death statistics with userId: %s and reason: %s",
                    userId, deathReason));
        }
    }

    public  void decrementUserLives(String userId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(queryBuilder.decrementUserLives(userId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while decrementing user lives with userId: %s",
                    userId));
        }
    }

}
