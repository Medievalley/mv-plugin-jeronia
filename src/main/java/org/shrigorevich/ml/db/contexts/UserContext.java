package org.shrigorevich.ml.db.contexts;

import org.shrigorevich.ml.domain.users.User;

import java.util.Optional;

public interface UserContext {
    Optional<User> getByName(String name);
}
