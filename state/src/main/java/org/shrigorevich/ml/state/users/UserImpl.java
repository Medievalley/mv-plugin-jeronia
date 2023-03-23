package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.users.UserJob;
import org.shrigorevich.ml.domain.users.UserRole;

import java.util.EnumMap;
import java.util.List;

public class UserImpl implements User {

    private final String id;
    private final String name;
    private final UserRole role;
    private final EnumMap<Job, UserJob> jobs;
    private int lives;

    public UserImpl(String id, String name, UserRole role, int lives, List<UserJobModel> jobs) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.lives = lives;
        this.jobs = new EnumMap<>(Job.class);
        jobs.forEach(userJobModel -> {
            Job job = Job.valueOf(userJobModel.getJobId());
            this.jobs.put(job, new UserJobImpl(userJobModel.getLevel()));
        });
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
    public EnumMap<Job, UserJob> getJobs(){
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
