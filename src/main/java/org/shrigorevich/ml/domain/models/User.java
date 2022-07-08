package org.shrigorevich.ml.domain.models;

public class User {
    private String name;
    private String lastIp;
    private int livesNumber;
    private boolean confirmed;
    private String roleName;

    public String getName() {
        return name;
    }

    public String getLastIp() {
        return lastIp;
    }

    public int getLivesNumber() {
        return livesNumber;
    }

    public boolean isConfirmed() {
        return confirmed;
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

    public void setLivesNumber(int livesNumber) {
        this.livesNumber = livesNumber;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
