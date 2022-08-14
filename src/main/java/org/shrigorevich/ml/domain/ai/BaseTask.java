package org.shrigorevich.ml.domain.ai;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTask implements Task {
    private final TaskPriority priority;
    private final Plugin plugin;
    private boolean inProgress;
    private final Entity entity;

    public BaseTask(Plugin plugin, TaskPriority priority, Entity entity) {
        this.priority = priority;
        this.plugin = plugin;
        this.entity = entity;
        this.inProgress = false;
    }

    @Override
    public boolean isInProgress() {
        return inProgress;
    }

    @Override
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public TaskPriority getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull Task task) {
        return this.priority.getValue() - task.getPriority().getValue();
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
