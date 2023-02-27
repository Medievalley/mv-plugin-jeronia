package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Entity;

public class PressureSpider extends CustomMobImpl {

    public PressureSpider(Entity mob, double power) {
        super(mob, power, MobType.PRESSURE_SPIDER);
    }

    @Override
    protected void setAI() {

    }
}
