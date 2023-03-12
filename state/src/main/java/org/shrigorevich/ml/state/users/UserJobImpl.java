package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.UserJob;

public class UserJobImpl implements UserJob {
    private int level;

    public UserJobImpl() {
        this.level = 1;
    }

    @Override
    public int getLevel(){
        return level;
    }
    @Override
    public void addLevel() { level++; }
    @Override
    public void removeLevel() { level--; }

}
