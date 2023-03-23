package org.shrigorevich.ml.state.mobs;

import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.mobs.MobType;

public class PressureSpider extends CustomMobImpl {

    public PressureSpider(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_SPIDER);
    }

    @Override
    public void setupAI() {

    }
}
