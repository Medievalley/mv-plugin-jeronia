package org.shrigorevich.ml.domain.ai.contracts;

import org.bukkit.entity.Entity;
import org.shrigorevich.ml.common.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService extends Service {
    void add(Task task);
    Optional<Task> get(UUID entityId);
    void finalize(UUID entityId);
    void block(UUID entityId);
    void clear(UUID entityId);
    void startTopPriority(UUID entityId);
    boolean shouldChangeTask(UUID entityId);
    void checkBlockedTasks(UUID entityId);
    void setDefaultAI(Entity entity);
    List<Task> getEntityTasks(UUID entityId);
}
