package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.shrigorevich.ml.domain.ai.Task;

import java.util.Optional;
import java.util.UUID;

class CustomMobImpl implements CustomMob {

    private final Entity entity;
    private double power;
    private Task task;

    public CustomMobImpl(Entity entity, double power) {
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
}
