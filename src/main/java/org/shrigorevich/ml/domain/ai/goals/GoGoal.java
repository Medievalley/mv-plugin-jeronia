package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class GoGoal implements Goal<Mob> {

    private final Location location;
    private Pathfinder.PathResult path;
    private final Mob mob;
    private int timer;
    private boolean isAchieved;
    public GoGoal(Mob mob, Location location) {
        this.location = location;
        this.mob = mob;
        this.isAchieved = false;
    }
    @Override
    public void start() {
        System.out.println("Go goal started");
        mob.getPathfinder().moveTo(path);
    }

    @Override
    public void stop() {
//        System.out.println("REACH_LOCATION stopped. Task: " + task.getType());
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= 20) {
            System.out.println("Points size: " + path.getPoints().size());
            System.out.println("Next point index: " + path.getNextPointIndex());
            timer = 0;
        }
        if (!isAchieved && path.getNextPointIndex() == 0 && path.getPoints().size() == 1) {
            System.out.println("Location reached");
            isAchieved = true;
        }
    }

    @Override
    public boolean shouldActivate() {
        this.path = mob.getPathfinder().findPath(location);
        return this.path != null;
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        //TODO: implement key
        return null;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}
