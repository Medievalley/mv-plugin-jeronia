package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;

import java.util.UUID;

class CustomMobImpl implements CustomMob {

    private final Entity entity;
    private final int power;

    public CustomMobImpl(Entity entity, int power) {
        this.entity = entity;
        this.power = power;
    }

    @Override
    public UUID getId() {
        return entity.getUniqueId();
    }

    @Override
    public int getPower() {
        return power;
    }
}
