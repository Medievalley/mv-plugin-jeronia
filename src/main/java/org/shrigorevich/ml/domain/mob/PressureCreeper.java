package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Mob;

public class PressureCreeper extends CustomMobImpl {

    public PressureCreeper(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_CREEPER);
    }

    @Override
    protected void setAI() {

    }
}
