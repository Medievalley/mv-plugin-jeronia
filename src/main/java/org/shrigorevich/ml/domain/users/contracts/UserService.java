package org.shrigorevich.ml.domain.users.contracts;

import org.bukkit.entity.EntityType;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.IAccessCheckCallback;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.models.UserModel;

import java.util.Optional;

public interface UserService extends Service {
    Optional<UserModel> getUser(String userName) throws Exception;
    void accessCheck(UserModel model, String ip, IAccessCheckCallback cb);
    Optional<User> getOnline(String name);
    void online(UserModel model);
    void offline(String name);
    void updateKillStatistics(String userName, EntityType entityType);
    void updateDeathStatistics(String userName, String deathReason);
    void decrementUserLives(String userName);
    void addUserJob(String userName, Job job, IResultCallback cb);
    void removeUserJob(String userName, Job job, IResultCallback cb);
}
