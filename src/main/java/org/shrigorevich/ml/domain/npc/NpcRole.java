package org.shrigorevich.ml.domain.npc;

public enum NpcRole {
    WARDEN(2), BUILDER(3);

    private final int roleId;

    NpcRole(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
