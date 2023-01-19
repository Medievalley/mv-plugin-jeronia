package org.shrigorevich.ml.domain.users;

import org.shrigorevich.ml.domain.structure.StructureType;

public enum UserRole {
    ADMIN(1), MODER(2), PLAYER(3);

    private final int roleId;

    UserRole(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
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
