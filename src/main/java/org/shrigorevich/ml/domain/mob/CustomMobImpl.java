package org.shrigorevich.ml.domain.mob;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.Task;

import java.util.UUID;

abstract class CustomMobImpl extends MemoryHolderImpl implements CustomMob {

    private final Mob entity;
    private final double power;
    private final MobType type;
    private Task task;

    public CustomMobImpl(Mob entity, double power, MobType type) {
        super();
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

    @Override
    public MobType getType() {
        return type;
    }

    @Override
    public Pathfinder getPathfinder() {
        return entity.getPathfinder();
    }

    protected Mob getMob() {
        return entity;
    }

    protected abstract void setAI();
}
