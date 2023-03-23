package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomNearestTargetGoal extends TargetGoalBase implements Goal<Mob> {
    protected final int randomInterval;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions targetConditions;
    private final GoalKey<Mob> key;
    public CustomNearestTargetGoal(Mob mob) {
        super(mob);
        this.randomInterval = reducedTickDelay(10);
        this.targetConditions = TargetingConditions.forCombat()
            .range(this.getFollowDistance()).selector(null);
        this.targetConditions.useFollowRange();
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "nearesttargetgoal"));
    }

    @Override
    public void start() {
        System.out.println("Nearest target started");
        this.mob.setTarget(target, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
        super.start();
    }

    @Override
    public boolean shouldActivate() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) { //TODO: adjust
            return false;
        } else {
            this.findTarget();
            return this.target != null;
        }
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    protected void findTarget() {
        this.target = this.mob.level.getNearestPlayer(
            this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }
}
