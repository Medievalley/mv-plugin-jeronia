package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Entity;
import org.shrigorevich.ml.domain.ai.Task;
import org.shrigorevich.ml.domain.mob.CustomMob;

import java.util.Optional;
import java.util.UUID;

abstract class CustomMobImpl implements CustomMob {

    private final Entity entity;
    private final double power;
    private final MobType type;
    private Task task;

    public CustomMobImpl(Entity entity, double power, MobType type) {
        this.entity = entity;
        this.power = power;
        this.type = type;
    }

    @Override
    public UUID getId() {
        return entity.getUniqueId();
    }

    @Override
    public double getPower() {
        return power;
    }

    protected Entity getEntity() {
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

    @Override
    public MobType getType() {
        return type;
    }
}
