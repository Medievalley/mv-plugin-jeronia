package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.MemoryUnit;
import org.shrigorevich.ml.domain.mobs.PointMemoryUnit;

import java.util.EnumSet;
import java.util.List;

public class ExploreGoal implements Goal<Mob> {

    private Pathfinder.PathResult path;
    private final CustomMob mob;
    private int timer;
    private boolean isAchieved;
    private final List<MemoryUnit> points;
    public ExploreGoal(CustomMob mob) {
        this.mob = mob;
        this.points = mob.getMemory(MemoryKey.INTEREST_POINT);
    }

    @Override
    public boolean shouldActivate() {
        return this.points.size() > 0;
    }

    @Override
    public void start() {
        for (MemoryUnit point : points) {
            path = mob.getPathfinder()
                .findPath(((PointMemoryUnit)point).getLocation());
            if (path != null) {
                mob.getPathfinder().moveTo(path);
                System.out.println("Go goal started");
                break;
            }
        }

    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
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
    public @NotNull GoalKey<Mob> getKey() {
        //TODO: implement key
        return null;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}
