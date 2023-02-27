package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Entity;

public class PressureZombie extends CustomMobImpl {

    public PressureZombie (Entity mob, double power) {
        super(mob, power, MobType.PRESSURE_ZOMBIE);
    }

    @Override
    protected void setAI() {

    }

}
