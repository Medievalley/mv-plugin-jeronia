package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.Task;
import org.shrigorevich.ml.domain.mob.CustomMob;

import java.util.Optional;
import java.util.UUID;

abstract class CustomMobImpl<T extends Mob> implements CustomMob<T> {

    private final T entity;
    private final double power;
    private Task task;

    public CustomMobImpl(T entity, double power) {
        this.entity = entity;
        this.power = power;
    }

    @Override
    public UUID getId() {
        return entity.getUniqueId();
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public void setTask(Task task) {
        if (this.task != null) {
            endTask();
        }
        this.task = task;
    }

    @Override
    public void startTask() {
        if (this.task != null) {
            task.start();
        }
    }

    @Override
    public Optional<Task> getTask() {
        return task == null ? Optional.empty() : Optional.of(task);
    }

    @Override
    public void endTask() {
        if (task != null) {
            task.end();
            task = null;
        }
    }

    protected abstract void setAI();
}
