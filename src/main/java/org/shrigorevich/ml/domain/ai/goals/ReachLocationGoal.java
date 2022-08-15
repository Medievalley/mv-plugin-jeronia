package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.TaskData;
import org.shrigorevich.ml.events.LocationReachedEvent;

import java.util.EnumSet;

public class ReachLocationGoal extends BaseGoal implements Goal<Mob> {
    private final Location target;

    public ReachLocationGoal(Plugin plugin, TaskData data, Mob mob, Location target) {

        super(mob, plugin, data, ActionKey.REACH_LOCATION);
        this.target = target;
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
        System.out.println(String.format("REACH_LOCATION activated. Task: %s. Target location: %d %d %d",
                getData().getType(), target.getBlockX(), target.getBlockY(), target.getBlockZ()));
    }

    @Override
    public void stop() {
//        System.out.println("REACH_LOCATION stopped. Task: " + getData().getType());
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {

        getMob().getPathfinder().moveTo(target, 0.7D);
        if (!isAchieved() && isLocationReached(getMob().getLocation())) {
            setAchieved(true);

            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                getPlugin().getServer().getPluginManager().callEvent(new LocationReachedEvent(getMob(), target, getData()));
            });
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

    private boolean isLocationReached(Location location) {
        return location.getBlockX() == target.getBlockX() &&
                location.getBlockY()+1 == target.getBlockY() &&
                location.getBlockZ() == target.getBlockZ();
    }
}