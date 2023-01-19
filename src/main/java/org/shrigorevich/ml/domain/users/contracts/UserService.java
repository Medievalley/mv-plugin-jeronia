package org.shrigorevich.ml.domain.users.contracts;

import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.IAccessCheckCallback;

import java.util.Optional;

public interface UserService extends Service {
    void accessCheck(String userName, String ip, IAccessCheckCallback cb);

    Optional<User> getFromOnlineList(String name);

    void addInOnlineList(User user);
    void removeFromOnlineList(String name);

}
