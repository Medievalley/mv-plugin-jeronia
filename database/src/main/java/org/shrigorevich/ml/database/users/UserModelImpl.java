package org.shrigorevich.ml.database.users;

import org.shrigorevich.ml.state.users.UserModel;

public class UserModelImpl implements UserModel {

    private String id;
    private String name;
    private String ip;
    private int lives;
    private boolean verified;
    private int roleId;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getLives() {
        return lives;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
