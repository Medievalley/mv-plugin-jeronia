package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BadHoldGoal extends BaseGoal implements Goal<Mob> {
    private final Location target;
    private int stuckTicks = 0;
    private int cooldown = 0;
    private Location stuckLocation;



    public BadHoldGoal(Plugin plugin, Mob mob, Location target) {
        super(mob, plugin, ActionKey.REACH_LOCATION);
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
        stuckLocation = getMob().getLocation();
        move();
    }

    @Override
    public void stop() {
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {

        cooldown+=1;
        if (cooldown == 10) {
            cooldown = 0;
            move();
        }

        if (Utils.isLocationsEquals(getMob().getLocation(), stuckLocation)) {
            stuckTicks+=1;
        } else {
            stuckLocation = getMob().getLocation();
        }
        if (stuckTicks == 20) {
            stuckTicks = 0;
            Pathfinder.PathResult path = getMob().getPathfinder().findPath(target);
            if (path != null && path.getNextPoint() != null) {
                getMob().teleport(path.getNextPoint());
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

    private void printLocation(Location l, String name) {
        System.out.printf("%s: %d %d %d%n", name, l.getBlockX(), l.getBlockY(), l.getBlockZ());
    };

    private void move() {
        getMob().getPathfinder().moveTo(target, 0.7D);
    };
}