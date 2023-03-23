package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.CustomMob;

import java.util.EnumSet;

public abstract class ExploreGoal extends CustomGoal implements Goal<Mob> {
    private final CustomMob mob;
    private int timer;
    private final int tickInterval;
    private final GoalKey<Mob> key;
    public ExploreGoal(CustomMob mob, int tickInterval) {
        this.mob = mob;
        this.tickInterval = tickInterval;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "exploregoal"));
    }
    @Override
    public void start() {
        System.out.println("Explore started");
    }

    @Override
    public void stop() {
        System.out.println("Explore stopped started");
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= tickInterval) {
            timer = 0;
        }
    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.UNKNOWN_BEHAVIOR);
    }
}
