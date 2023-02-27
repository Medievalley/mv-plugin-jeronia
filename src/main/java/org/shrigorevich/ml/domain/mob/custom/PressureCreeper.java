package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Entity;

public class PressureCreeper extends CustomMobImpl {

    public PressureCreeper(Entity mob, double power) {
        super(mob, power, MobType.PRESSURE_CREEPER);
    }

    @Override
    protected void setAI() {

    }
}
