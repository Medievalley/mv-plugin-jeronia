package org.shrigorevich.ml.state.users;

import java.util.List;
import java.util.Optional;

public interface UserContext {
    Optional<UserModel> getByName(String name) throws Exception;
    void updateKillStatistics(String userId, String entityType) throws Exception;
    void updateDeathStatistics(String userId, String deathReason) throws Exception;
    void decrementUserLives(String userId) throws Exception;
    void addUserJob(String userId, int jobId) throws Exception;
    void removeUserJob(String userId, int jobId) throws Exception;
    List<UserJobModel> getUserJobsByUserId(String userId) throws Exception;
    List<RestrictedItemModel> getRestrictedItems() throws Exception;
}
