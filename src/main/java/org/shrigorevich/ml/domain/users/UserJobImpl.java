package org.shrigorevich.ml.domain.users;

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
