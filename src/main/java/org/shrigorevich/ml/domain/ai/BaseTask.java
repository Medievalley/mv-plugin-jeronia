package org.shrigorevich.ml.domain.ai;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTask implements Task {
    private final TaskPriority priority;
    private final Plugin plugin;
    private boolean inProgress;
    private final Entity entity;
    private final TaskData data;

    public BaseTask(Plugin plugin, TaskType type, TaskPriority priority, Entity entity) {
        this.priority = priority;
        this.plugin = plugin;
        this.entity = entity;
        this.inProgress = false;
        this.data = new TaskDataImpl(type);
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
    public TaskData getData() {
        return data;
    }

    @Override
    public int compareTo(@NotNull Task task) {
        return task.getPriority().getValue() - this.priority.getValue();
    }



    public Plugin getPlugin() {
        return plugin;
    }
}
