package org.shrigorevich.ml.domain.ai;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    void add(PriorityTask task);
    Optional<PriorityTask> get(UUID entityId);
    void finalize(UUID entityId);
    void block(UUID entityId);
    void clear(UUID entityId);
    void startTopPriority(UUID entityId);
    boolean shouldChangeTask(UUID entityId);
    void checkBlockedTasks(UUID entityId);
    void setDefaultAI(Entity entity);
    List<PriorityTask> getEntityTasks(UUID entityId);
    Plugin getPlugin();
}
