package org.shrigorevich.ml.state.mobs;

import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.mobs.MobType;

public class PressureSkeleton extends ValleyMobImpl {

    public PressureSkeleton(Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_SKELETON);
    }

    @Override
    public void setupAI() {

    }
}
