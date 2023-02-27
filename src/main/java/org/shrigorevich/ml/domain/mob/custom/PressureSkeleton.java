package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Entity;

public class PressureSkeleton extends CustomMobImpl {

    public PressureSkeleton(Entity mob, double power) {
        super(mob, power, MobType.PRESSURE_SKELETON);
    }

    @Override
    protected void setAI() {

    }
}
