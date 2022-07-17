package org.shrigorevich.ml.domain.models;

public class User {

    private int id;
    private String name;
    private String lastIp;
    private int lives;
    private boolean verified;
    private String roleName;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getLastIp() {
        return lastIp;
    }

    public int getLives() {
        return lives;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
