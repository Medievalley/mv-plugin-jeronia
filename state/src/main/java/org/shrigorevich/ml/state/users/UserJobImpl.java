package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.UserJob;

public class UserJobImpl implements UserJob {
    private Job job;
    private int level;

    public UserJobImpl() {
        this.level = 1;
    }

    public UserJobImpl(Job job, int level) {
        this.level = level;
        this.job = job;
    }

    @Override
    public int getLevel(){
        return level;
    }

    public Job getJob() { return job; }

    public void addLevel() { level++; }

    public void removeLevel() { level--; }
}
