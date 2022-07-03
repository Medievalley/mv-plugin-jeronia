package org.shrigorevich.ml.db.contexts;

import org.shrigorevich.ml.db.callbacks.IFindOneCallback;
import org.shrigorevich.ml.db.models.User;

import java.util.Optional;

public interface IUserContext {
    void getByNameAsync(String name, IFindOneCallback<User> callback);
    User getByName(String name);
    User getByNameInMemory(String name);
    void addInMemory(User user);
}
