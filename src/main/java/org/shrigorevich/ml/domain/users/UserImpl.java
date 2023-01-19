package org.shrigorevich.ml.domain.users;

import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.models.UserModel;

public class UserImpl implements User {

    private final int id;
    private final String name;
    private final UserRole role;
    private int lives;

    //TODO: handle nullable role
    public UserImpl(UserModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.role = UserRole.valueOf(model.getRoleId());
        this.lives = model.getLives();
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
