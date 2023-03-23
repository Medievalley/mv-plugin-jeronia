package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.MemoryUnit;
import org.shrigorevich.ml.domain.mobs.PointMemoryUnit;

import java.util.EnumSet;
import java.util.List;
import java.util.Queue;

public class WalkGoal extends CustomGoal implements Goal<Mob> {

    private Pathfinder.PathResult path;
    private final CustomMob mob;
    private final net.minecraft.world.entity.Mob handle;
    private Queue<Location> points;
    private final int checkArrivalInterval;
    private final GoalKey<Mob> key;
    public WalkGoal(CustomMob mob) {
        this.mob = mob;
        this.points = mob.getRoutePoints();
        this.handle = ((CraftMob) mob.getHandle()).getHandle();
        this.checkArrivalInterval = 20;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "walkgoal"));
    }

    @Override
    public boolean shouldActivate() {
//        this.points = mob.getRoutePoints();
        return this.points.size() > 0;
    }

    @Override
    public void start() {
        defineTargetLocation();
        if (path != null) {
            System.out.println("Walk goal started with path");
        } else {
            System.out.println("Walk goal started without path");
        }
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        if (this.checkArrivalInterval > 0 && this.handle.getRandom().nextInt(this.checkArrivalInterval) != 0) { //TODO: adjust
            if (path == null || locationReached(path.getFinalPoint())) {
                defineTargetLocation();
                if (path != null) {
                    mob.getPathfinder().moveTo(path);
                    System.out.println("Walk goal destination changed: " + locToString(path.getFinalPoint()));
                } else {
                    System.out.println("Walk goal destination not available");
                }
            }
        }
        
//        if (path.getNextPointIndex() == 0 && path.getPoints().size() == 1) {
//            System.out.println("Location reached");
//        }
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }

    private boolean locationReached(Location l) {
        return mob.getLocation().distanceSquared(l) < 1;
    }

    private void defineTargetLocation() {
        Location loc = points.peek();
        path = mob.getPathfinder().findPath(loc);
        if (path != null) {
            mob.getPathfinder().moveTo(path);
            points.remove();
            points.add(loc);
            System.out.printf("Next point: %s. Locs: %d%n", locToString(loc), points.size());
        }
    }
}
