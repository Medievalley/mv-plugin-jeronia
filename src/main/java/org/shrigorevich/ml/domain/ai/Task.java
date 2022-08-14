package org.shrigorevich.ml.domain.ai;

import org.bukkit.entity.Entity;

public interface Task extends Comparable<Task> {
    TaskPriority getPriority();
    Entity getEntity();
    void setInProgress(boolean inProgress);
    boolean isInProgress();
}
