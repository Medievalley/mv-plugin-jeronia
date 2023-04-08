package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;

import java.util.EnumSet;
import java.util.List;

public class WalkGoal extends ExploreGoal implements Goal<Mob> {

    private Pathfinder.PathResult path;
    private final CustomMob mob;
    private final net.minecraft.world.entity.Mob handle;
    private final int checkArrivalInterval;
    private final int recalculatePathInterval;
    private final GoalKey<Mob> key;
    private double speed;
    private LocationMemoryUnit currentDestination;
    public WalkGoal(CustomMob mob) {
        super(mob, 25);
        this.mob = mob;
        this.handle = ((CraftMob) mob.getHandle()).getHandle();
        this.checkArrivalInterval = 30;
        this.recalculatePathInterval = 25;
        this.speed = 1.1D;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", "walkgoal"));
    }

    @Override
    public boolean shouldActivate() {
        return getRoute().size() > 0;
    }

    @Override
    public void start() {
        defineNextDestination();
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.recalculatePathInterval > 0 && this.handle.getRandom().nextInt(this.recalculatePathInterval) == 0) {
            if (currentDestination != null) {
                path = mob.getPathfinder().findPath(currentDestination.getLocation());
            }
        }
        if (this.checkArrivalInterval > 0 && this.handle.getRandom().nextInt(this.checkArrivalInterval) == 0) {
            if (path == null || locationReached(path.getFinalPoint())) {
                if (currentDestination != null)
                    currentDestination.setVisited(true);
                defineNextDestination();
            } else {
                move(); //TODO: maybe unnecessary
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
        return mob.getLocation().distance(l) < 1.5;
    }

    private void defineNextDestination() {
        LocationMemoryUnit next = findClosest(mob.getLocation(), getRoute(), (unit) -> !unit.isVisited());
        if (next != null) {
            path = mob.getPathfinder().findPath(next.getLocation());
            if (path != null) {
                currentDestination = next;
                move();
//                System.out.printf("Next point: %s. Locs: %d%n", locToString(next.getLocation()), getRoute().size());
                System.out.println("Walk goal destination changed: " + locToString(path.getFinalPoint()));
            } else {
                System.out.println("Walk goal destination not available");
            }
        } else {
            resetRoute();
        }
    }

    private void move() {
        mob.getPathfinder().moveTo(path, speed);
    }

    private List<LocationMemoryUnit> getRoute() {
        return this.mob.getMemories(MemoryKey.ROUTE_POINT);
    }

    private void resetRoute() {
        for (LocationMemoryUnit unit : getRoute()) {
            unit.setVisited(false);
        }
        System.out.println("Walk goal: route reseted");
    }
}
