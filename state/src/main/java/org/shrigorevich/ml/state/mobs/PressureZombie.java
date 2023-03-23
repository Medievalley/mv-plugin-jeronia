package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.entity.ai.MobGoals;
import net.minecraft.core.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftZombie;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.goals.CustomNearestTargetGoal;
import org.shrigorevich.ml.domain.ai.goals.CustomZombieAttackGoal;
import org.shrigorevich.ml.domain.ai.goals.WalkGoal;
import org.shrigorevich.ml.domain.mobs.MobType;

public class PressureZombie extends CustomMobImpl {

    public PressureZombie (Mob mob, double power) {
        super(mob, power, MobType.PRESSURE_ZOMBIE);
    }

    @Override
    public void setupAI() {
        removeVanillaAI();
        getHandle().getPathfinder().setCanOpenDoors(false);
        getHandle().getPathfinder().setCanPassDoors(true);
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.addGoal(getHandle(), 2, new WalkGoal(this));
        goals.addGoal(getHandle(), 1, new CustomNearestTargetGoal(getHandle()));
        goals.addGoal(getHandle(), 1, new CustomZombieAttackGoal(((CraftZombie) getHandle()).getHandle(), 1.0D));
//        goals.addGoal(getHandle(), 1, new TestGoal(this, "test1", 100, GoalType.UNKNOWN_BEHAVIOR));
//        goals.addGoal(getHandle(), 1, new TestGoal(this, "test2", 200, GoalType.UNKNOWN_BEHAVIOR));
//        goals.addGoal(getHandle(), 2, new TestGoal(this, "test3", 200, GoalType.UNKNOWN_BEHAVIOR));
    }
}
