package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.User;

public interface Restriction {
    boolean isAllowed(User user);
}
