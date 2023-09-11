package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ValleyZombieAttackGoal extends ValleyMeleeAttackGoal implements Goal<Mob> {
    private final Zombie zombie;
    private int raiseArmTicks;
    private final GoalKey<Mob> key;
    public ValleyZombieAttackGoal(Zombie zombie, double speed) {
        super(zombie, speed);
        this.zombie = zombie;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "customzombieattackgoal"));
    }

    @Override
    public void start() {
        zombie.getNavigation().pathFinder.nodeEvaluator.setCanOpenDoors(true);
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        } else {
            this.zombie.setAggressive(false);
        }

    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }
}
