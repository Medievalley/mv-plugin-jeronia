package org.shrigorevich.ml.domain.ai;

import java.util.Optional;

public interface TaskHolder {
    /**
     * Set a task to this holder
     * @param task
     */
    void setTask(Task task);

    void startTask();
    Optional<Task> getTask();
    void endTask();
}
