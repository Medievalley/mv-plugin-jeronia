package org.shrigorevich.ml.domain.users.contracts;

import org.shrigorevich.ml.domain.users.UserRole;

public interface User {
    int getId();
    String getName();
    UserRole getRole();
    int getLives();
    void addLive();
    void removeLive();
}
