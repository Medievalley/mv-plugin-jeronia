package org.shrigorevich.ml.domain.users;

public enum UserRole {
    ADMIN(1, 3), MODER(2, 2), PLAYER(3, 1);

    private final int roleId;
    private final int accessLevel;

    UserRole(int roleId, int accessLevel) {
        this.roleId = roleId;
        this.accessLevel = accessLevel;
    }

    public int getRoleId() {
        return roleId;
    }

    public int accessLevel() {
        return accessLevel;
    }

    public static UserRole valueOf(int id) {
        for(UserRole role : UserRole.values()) {
            if (role.getRoleId() == id) {
                return role;
            }
        }
        return null;
    }
}
