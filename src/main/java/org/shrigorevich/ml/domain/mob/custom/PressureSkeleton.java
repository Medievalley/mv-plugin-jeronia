package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Skeleton;

public class PressureSkeleton extends CustomMobImpl<Skeleton> {

    public PressureSkeleton(Skeleton mob, double power) {
        super(mob, power);
    }

    @Override
    protected void setAI() {

    }
}
