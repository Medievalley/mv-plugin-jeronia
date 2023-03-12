package org.shrigorevich.ml.domain.users;

import java.util.EnumMap;

public interface User {
    String getId();
    String getName();
    UserRole getRole();
    int getLives();
    void addLive();
    void removeLive();
    EnumMap<Job, UserJob> getJobs();
    void addJob (Job job);
    void removeJob (Job job);
}
