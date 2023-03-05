package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Mob;

public class PressureSpider extends CustomMobImpl {

    public PressureSpider(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_SPIDER);
    }

    @Override
    protected void setAI() {

    }
}
