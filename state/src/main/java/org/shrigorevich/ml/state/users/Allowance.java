package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.User;

public interface Allowance {
    boolean isMathed(User user);
}
