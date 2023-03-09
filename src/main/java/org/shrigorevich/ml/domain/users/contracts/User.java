package org.shrigorevich.ml.domain.users.contracts;

import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.UserJobImpl;
import org.shrigorevich.ml.domain.users.UserRole;

import java.util.EnumMap;

public interface User {
    String getId();
    String getName();
    UserRole getRole();
    int getLives();
    void addLive();
    void removeLive();
    EnumMap<Job, UserJobImpl> getJobs();
    void addJob (Job job);
    void removeJob (Job job);
}
