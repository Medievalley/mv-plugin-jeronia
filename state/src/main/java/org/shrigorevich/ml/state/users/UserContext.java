package org.shrigorevich.ml.state.users;

import java.util.Optional;

public interface UserContext {
    Optional<UserModel> getByName(String name) throws Exception;

    void updateKillStatistics(String userId, String entityType) throws Exception;
    void updateDeathStatistics(String userId, String deathReason) throws Exception;
    void decrementUserLives(String userId) throws Exception;
}
