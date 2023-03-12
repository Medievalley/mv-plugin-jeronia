package org.shrigorevich.ml.domain.users;

import org.bukkit.entity.EntityType;
import org.shrigorevich.ml.common.callback.IAccessCheckCallback;
import org.shrigorevich.ml.common.callback.IResultCallback;
import org.shrigorevich.ml.domain.Service;

import java.util.Optional;

public interface UserService extends Service {
    void accessCheck(String name, String ip, IAccessCheckCallback cb);
    Optional<User> getOnline(String name);
    void offline(String name);
    void updateKillStatistics(String userName, EntityType entityType);
    void updateDeathStatistics(String userName, String deathReason);
    void decrementUserLives(String userName);
    void addUserJob(String userName, Job job, IResultCallback cb);
    void removeUserJob(String userName, Job job, IResultCallback cb);
}
