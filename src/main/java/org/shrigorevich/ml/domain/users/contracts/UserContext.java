package org.shrigorevich.ml.domain.users.contracts;

import org.shrigorevich.ml.domain.users.models.UserModel;

import java.util.Optional;

public interface UserContext {
    Optional<UserModel> getByName(String name) throws Exception;

    void updateKillStatistics(String userId, String entityType) throws Exception;
    void updateDeathStatistics(String userId, String deathReason) throws Exception;
}
