package org.shrigorevich.ml.domain.mob.custom;

import org.bukkit.entity.Zombie;

public class PressureZombie extends CustomMobImpl<Zombie> {

    public PressureZombie (Zombie mob, double power) {
        super(mob, power);
    }

    @Override
    protected void setAI() {

    }

}
