package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.users.UserRole;

public class RoleAllowance implements Allowance{

    private UserRole roleRequired;

    public void setRoleId(int roleId) {
        this.roleRequired = UserRole.valueOf(roleId);
    }

    public UserRole getRole() {
        return roleRequired;
    }

    @Override
    public boolean isMathed(User user) {
        return user.getRole() == roleRequired;
    }
}
