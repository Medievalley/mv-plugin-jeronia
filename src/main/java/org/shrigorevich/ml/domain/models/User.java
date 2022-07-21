package org.shrigorevich.ml.domain.models;

public class User {

    private int id;
    private String name;
    private String ip;
    private int lives;
    private boolean verified;
    private String roleName;

    public int getId() {
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

    public String getRoleName() {
        return roleName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
