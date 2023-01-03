package org.shrigorevich.ml.domain.ai.contracts;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.TaskType;

public interface Task extends Comparable<Task> {
    TaskPriority getPriority();
    Mob getEntity();
    void setBlocked(boolean blocked);
    boolean isBlocked();
    void start();
    void end();
    boolean shouldBeBlocked();
    TaskType getType();
}
