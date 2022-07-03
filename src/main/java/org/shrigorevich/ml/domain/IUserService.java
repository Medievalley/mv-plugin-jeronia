package org.shrigorevich.ml.domain;

import org.shrigorevich.ml.db.callbacks.IAccessCheckCallback;
import org.shrigorevich.ml.db.models.User;

import java.util.Optional;

public interface IUserService {
    void accessCheck(String userName, String ip, IAccessCheckCallback cb);

    Optional<User> getByNameInMemory(String name);

    void addInMemory(User user);

}
