package org.shrigorevich.ml.domain.ai;
import org.bukkit.entity.Mob;

public interface Task extends Comparable<Task> {
    TaskPriority getPriority();
    Mob getEntity();
    TaskData getData();
    void setBlocked(boolean blocked);
    boolean isBlocked();
    void start();
    void end();
    boolean shouldBeBlocked();
    TaskType getType();
}
