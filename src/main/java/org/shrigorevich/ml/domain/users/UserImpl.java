package org.shrigorevich.ml.domain.users;

import org.shrigorevich.ml.domain.users.contracts.User;
import java.util.EnumMap;

public class UserImpl implements User {

    private final String id;
    private final String name;
    private final UserRole role;
    private EnumMap<Job, UserJobImpl> jobs;
    private int lives;

    public UserImpl(String id, String name, UserRole role, int lives) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.lives = lives;
        this.jobs = new EnumMap<Job, UserJobImpl>(Job.class);
    }

    @Override
    public String getId() {
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

    @Override
    public EnumMap<Job, UserJobImpl> getJobs(){
        return jobs;
    }

    @Override
    public void addJob(Job job) {
        jobs.put(job, new UserJobImpl());
    }

    @Override
    public void removeJob(Job job) {
        jobs.remove(job);
    }
}
