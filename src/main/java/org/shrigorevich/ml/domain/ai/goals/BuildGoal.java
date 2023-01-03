package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.contracts.BuildTask;
import org.shrigorevich.ml.events.LocationReachedEvent;

import java.util.EnumSet;

public class BuildGoal extends BaseGoal implements Goal<Mob> {
    private final BuildTask task;

    public BuildGoal(Plugin plugin, BuildTask task, Mob mob, Location target) {
        super(
            mob, target,
            new LocationReachedEvent(mob, target, task),
            plugin, ActionKey.REACH_LOCATION);
        this.task = task;
    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }

    @Override
    public void start() {
        move(0.7D);
    }

    @Override
    public void stop() {
//        System.out.println("Build stopped. Task: " + task.getType());
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        defaultTick();
    }

    @Override
    public GoalKey<Mob> getKey() {
        return getGoalKey();
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}