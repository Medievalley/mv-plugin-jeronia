package org.shrigorevich.ml.database.users;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.database.BaseContext;
import org.shrigorevich.ml.state.users.RestrictedItemModel;
import org.shrigorevich.ml.state.users.UserContext;
import org.shrigorevich.ml.state.users.UserJobModel;
import org.shrigorevich.ml.state.users.UserModel;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
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

    public void addUserJob(String userId, int jobId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(queryBuilder.addUserJob(userId, jobId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while adding user job with userId: %s, jobId: %d",
                    userId, jobId));
        }
    }

    public void removeUserJob(String userId, int jobId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(queryBuilder.removeUserJob(userId, jobId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while removing user job with userId: %s, jobId: %d",
                    userId, jobId));
        }
    }

    public List<UserJobModel> getUserJobsByUserId(String userId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<UserJobModel>> h = new BeanListHandler<>(UserJobModelImpl.class);
            return run.query(queryBuilder.getUserJobsByUserId(userId), h);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while getting user jobs userId: %s", userId));
        }
    }

    @Override
    public List<RestrictedItemModel> getRestrictedItems() throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<RestrictedItemModel>> h = new BeanListHandler<>(RestrictedItemModelImpl.class);
            return run.query(queryBuilder.getRestrictedItems(), h);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception("Error while getting restricted items");
        }
    }
}
