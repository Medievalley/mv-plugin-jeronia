package org.shrigorevich.ml.domain.users;

import org.shrigorevich.ml.domain.users.contracts.User;

public class UserImpl implements User {

    private final int id;
    private final String name;
    private final UserRole role;
    private int lives;

    public UserImpl(int id, String name, UserRole role, int lives) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.lives = lives;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void addLive() {
        lives++;
    }

    @Override
    public void removeLive() {
        lives--;
    }
}
