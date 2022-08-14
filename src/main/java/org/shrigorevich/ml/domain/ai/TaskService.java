package org.shrigorevich.ml.domain.ai;

import org.bukkit.entity.Entity;
import org.shrigorevich.ml.domain.Service;

import java.util.UUID;

public interface TaskService extends Service {
    void add(NpcTask task);
    void finalizeCurrent(UUID entityId);
    void startTopPriority(UUID entityId);
    boolean shouldChangeTask(UUID entityId);
    void setDefaultAI(Entity entity);
}
