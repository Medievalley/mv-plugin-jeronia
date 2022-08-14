package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class VigilantGoal implements Goal<Villager> {

    @Override
    public boolean shouldActivate() {
        return false;
    }

    @Override
    public boolean shouldStayActive() {
        return Goal.super.shouldStayActive();
    }

    @Override
    public void start() {
        Goal.super.start();
    }

    @Override
    public void stop() {
        Goal.super.stop();
    }

    @Override
    public void tick() {
        Goal.super.tick();
    }

    @Override
    public @NotNull GoalKey<Villager> getKey() {
        return null;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return null;
    }
}
