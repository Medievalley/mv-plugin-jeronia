package org.shrigorevich.ml.domain.ai;
import org.bukkit.entity.Mob;

public interface Task {
    Mob getEntity();
    void start();
    void end();
    TaskType getType();
}
