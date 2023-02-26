package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Creeper;

public class PressureCreeper extends CustomMobImpl<Creeper> {

    public PressureCreeper(Creeper mob, double power) {
        super(mob, power);
    }

    @Override
    protected void setAI() {

    }
}
