package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.BuildTask;
import org.shrigorevich.ml.domain.ai.Task;
import org.shrigorevich.ml.events.LocationReachedEvent;
import org.shrigorevich.ml.events.UnableToReachLocationEvent;

import java.util.EnumSet;

public class BuildGoal extends BaseGoal implements Goal<Mob> {
    private final Location target;
    private final BuildTask task;
    private int cooldown = 0;

    public BuildGoal(Plugin plugin, BuildTask task, Mob mob, Location target) {
        super(mob, plugin, ActionKey.REACH_LOCATION);
        this.target = target;
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
        System.out.printf("REACH_LOCATION activated. Task: %s. Target location: %d %d %d%n",
                task.getType(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
        getMob().getPathfinder().moveTo(target, 0.7D);
    }

    @Override
    public void stop() {
        System.out.println("REACH_LOCATION stopped. Task: " + task.getType());
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        getMob().getPathfinder().moveTo(target, 0.7D);

        if (getMob().getPathfinder().hasPath()) {
            Location mLoc = getMob().getLocation();
            Location finalLocation = getMob().getPathfinder().getCurrentPath().getFinalPoint();

            if (!isAchieved() && Utils.isLocationsEquals(mLoc, finalLocation)) {
                setAchieved(true);
                getPlugin().getServer().getPluginManager()
                        .callEvent(new LocationReachedEvent(getMob(), target, task));
            }
        }
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