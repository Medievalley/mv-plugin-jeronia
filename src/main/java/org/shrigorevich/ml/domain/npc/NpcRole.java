package org.shrigorevich.ml.domain.npc;

public enum NpcRole {
    HARVESTER(1), WARDEN(2);

    private final int roleId;

    NpcRole(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
