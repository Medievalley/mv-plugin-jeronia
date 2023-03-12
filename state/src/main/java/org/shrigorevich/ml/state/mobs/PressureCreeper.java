package org.shrigorevich.ml.state.mobs;

import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.mobs.MobType;

public class PressureCreeper extends CustomMobImpl {

    public PressureCreeper(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_CREEPER);
    }

    @Override
    protected void setAI() {

    }
}
