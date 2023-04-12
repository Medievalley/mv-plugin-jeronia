package org.shrigorevich.ml.domain.users;

import java.util.EnumMap;
import java.util.List;

public interface User {
    String getId();
    String getName();
    UserRole getRole();
    int getLives();
    void addLive();
    void removeLive();
    List<UserJob> getJobsInfo();
    List<Job> getJobs();
    void addJob (Job job);
    void removeJob (Job job);
}
