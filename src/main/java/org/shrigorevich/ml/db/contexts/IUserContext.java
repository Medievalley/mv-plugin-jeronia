package org.shrigorevich.ml.db.contexts;

import org.shrigorevich.ml.domain.models.User;

import java.util.Optional;

public interface IUserContext {
    Optional<User> getByName(String name);
}
