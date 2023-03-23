package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class GoGoal implements Goal<Mob> {

    private final Location location;
    private Pathfinder.PathResult path;
    private final Mob mob;
    private int timer;
    private boolean isAchieved;
    private final Plugin plugin;
    public GoGoal(Mob mob, Location location, Plugin plugin) {
        this.location = location;
        this.mob = mob;
        this.isAchieved = false;
        this.plugin = plugin;
    }
    @Override
    public void start() {
        System.out.println("Go goal started");
        mob.getPathfinder().moveTo(path);
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        timer++;
        if (timer >= 20) {
//            System.out.println("Points size: " + path.getPoints().size());
//            System.out.println("Next point index: " + path.getNextPointIndex());
            if (mob.getPathfinder().hasPath()) {
//                System.out.println("Is done: " + ((CraftMob) mob).getHandle().getNavigation().getPath().isDone());
            }
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