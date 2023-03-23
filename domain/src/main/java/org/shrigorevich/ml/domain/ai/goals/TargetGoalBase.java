package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.scores.Team;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class TargetGoalBase extends CustomGoal implements Goal<Mob> {

    protected final net.minecraft.world.entity.Mob mob;
    protected final boolean mustSee;
    private final boolean mustReach;
    private int reachCache;
    private int reachCacheTime;
    private int unseenTicks;
    protected int unseenMemoryTicks;
    private final GoalKey<Mob> key;
    public TargetGoalBase(Mob entity) {
        this.mob = ((CraftMob) entity).getHandle();
        this.mustSee = true;
        this.mustReach = true;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "nearesttarget"));
        this.unseenMemoryTicks = 60;
    }

    @Override
    public void start() {
        this.reachCache = 0;
        this.reachCacheTime = 0;
        this.unseenTicks = 0;
        System.out.println("NearestTarget started");
    }

    @Override
    public void stop() {
        this.mob.setTarget((LivingEntity) null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true); // CraftBukkit
        System.out.println("NearestTarget stopped");
    }

    @Override
    public boolean shouldStayActive() {
        LivingEntity entityliving = this.mob.getTarget();

        if (entityliving == null) {
            return false;
        } else if (!this.mob.canAttack(entityliving)) {
            return false;
        } else {
            Team scoreboardteambase = this.mob.getTeam();
            Team scoreboardteambase1 = entityliving.getTeam();

            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.getFollowDistance();

                if (this.mob.distanceToSqr(entityliving) > d0 * d0) {
                    return false;
                } else {
                    if (this.mustSee) {
                        if (this.mob.getSensing().hasLineOfSight(entityliving)) {
                            this.unseenTicks = 0;
                        } else if (++this.unseenTicks > this.unseenMemoryTicks) {
                            return false;
                        }
                    }

                    this.mob.setTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // CraftBukkit
                    return true;
                }
            }
        }
    }

    protected boolean canAttack(@Nullable LivingEntity target, TargetingConditions targetPredicate) {
        if (target == null) {
            return false;
        } else if (!targetPredicate.test(this.mob, target)) {
            return false;
        } else if (!this.mob.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            if (this.mustReach) {
                if (--this.reachCacheTime <= 0) {
                    this.reachCache = 0;
                }

                if (this.reachCache == 0) {
                    this.reachCache = this.canReach(target) ? 1 : 2;
                }

                if (this.reachCache == 2) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canReach(LivingEntity entity) {
        this.reachCacheTime = 10 + this.mob.getRandom().nextInt(5);
        Path pathentity = this.mob.getNavigation().createPath((Entity) entity, 0);

        if (pathentity == null) {
            return false;
        } else {
            Node pathpoint = pathentity.getEndNode();

            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.x - entity.getBlockX();
                int j = pathpoint.z - entity.getBlockZ();

                return (double) (i * i + j * j) <= 2.25D;
            }
        }
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.TARGET);
    }
}