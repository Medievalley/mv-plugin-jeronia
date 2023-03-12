package org.shrigorevich.ml.database.users;

import org.shrigorevich.ml.state.users.UserJobModel;

public class UserJobModelImpl implements UserJobModel {
    private int jobId;
    private int level;
    @Override
    public int getJobId() {
        return jobId;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setJobId(int value) {
        jobId = value;
    }

    @Override
    public void setLevel(int value) {
        level = value;
    }
}
