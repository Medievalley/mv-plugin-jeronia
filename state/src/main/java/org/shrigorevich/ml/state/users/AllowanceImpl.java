package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.User;

public class AllowanceImpl implements Allowance{
    @Override
    public boolean isMathed(User user) {
        return false;
    }
}
