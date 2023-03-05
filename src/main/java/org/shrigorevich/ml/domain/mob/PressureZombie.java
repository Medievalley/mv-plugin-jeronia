package org.shrigorevich.ml.domain.mob;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.goals.ExploreGoal;

public class PressureZombie extends CustomMobImpl {

    public PressureZombie (Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_ZOMBIE);
    }

    @Override
    protected void setAI() {
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.addGoal(getMob(), 1, new ExploreGoal(this));
    }

}
