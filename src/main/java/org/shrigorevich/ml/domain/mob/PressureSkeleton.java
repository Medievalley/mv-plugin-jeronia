package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Mob;

public class PressureSkeleton extends CustomMobImpl {

    public PressureSkeleton(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_SKELETON);
    }

    @Override
    protected void setAI() {

    }
}
