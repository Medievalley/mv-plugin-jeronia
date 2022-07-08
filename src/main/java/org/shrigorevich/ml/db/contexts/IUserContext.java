package org.shrigorevich.ml.db.contexts;

import org.shrigorevich.ml.db.callbacks.IFindOneCallback;
import org.shrigorevich.ml.domain.models.User;

public interface IUserContext {
    void getByNameAsync(String name, IFindOneCallback<User> callback);
    User getByName(String name);
}
