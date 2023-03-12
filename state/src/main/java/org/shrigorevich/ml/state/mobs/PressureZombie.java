package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.goals.ExploreGoal;
import org.shrigorevich.ml.domain.mobs.MobType;

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
