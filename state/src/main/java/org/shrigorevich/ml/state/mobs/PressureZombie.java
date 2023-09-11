package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftZombie;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.goals.*;
import org.shrigorevich.ml.domain.mobs.MobType;

public class PressureZombie extends ValleyMobImpl {

    public PressureZombie (Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_ZOMBIE);
    }

    @Override
    public void setupAI() {
        removeVanillaAI();
        getHandle().getPathfinder().setCanOpenDoors(false);
        getHandle().getPathfinder().setCanPassDoors(true);
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.addGoal(getHandle(), 1, new ValleyNearestTargetGoal(getHandle()));
        goals.addGoal(getHandle(), 1, new ValleyZombieAttackGoal(((CraftZombie) getHandle()).getHandle(), 1.0D));
        goals.addGoal(getHandle(), 2, new ValleyBreakDoorGoal(this));
        goals.addGoal(getHandle(), 3, new ZombieHarmGoal(this));
        goals.addGoal(getHandle(), 4, new WalkGoal(this));
    }
}
