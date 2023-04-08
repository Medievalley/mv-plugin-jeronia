package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.common.MaterialHelper;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;
import org.shrigorevich.ml.domain.mobs.MemoryKey;

import java.util.EnumSet;
import java.util.List;

public class ZombieHarmGoal extends CustomGoal implements Goal<Mob> {

    private final CustomMob mob;
    private final net.minecraft.world.entity.Mob handle;
    private final double speed;
    private int updateTargetInterval;
    private final GoalKey<Mob> key;
    private Pathfinder.PathResult path;
    private LocationMemoryUnit currentDestination;
    public ZombieHarmGoal(CustomMob mob) {
        this.mob = mob;
        this.handle = ((CraftMob) mob.getHandle()).getHandle();
        this.updateTargetInterval = 15;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("mv", "zombieharmgoal"));
        this.speed = 0.85D;
    }

    @Override
    public boolean shouldActivate() {
        return getCrops().size() > 0;
    }

    @Override
    public void start() {
        defineNextDestination();
    }

    @Override
    public void stop() {
    }

    @Override
    public void tick() {
        //TODO: need to rise hands when attack crop
        if (this.updateTargetInterval > 0 && this.handle.getRandom().nextInt(this.updateTargetInterval) == 0) {
            if (currentDestination != null && locationReached()){
                mob.getHandle().getWorld().getBlockAt(currentDestination.getLocation()).breakNaturally();
                getCrops().remove(currentDestination);
                System.out.println("Crops size updated: " + getCrops().size());
                defineNextDestination();
            } else if(currentDestination == null || path == null){
                defineNextDestination();
            }
        }
    }

    private void defineNextDestination() {

        LocationMemoryUnit next = findClosest(mob.getLocation(), getCrops(),
                unit -> MaterialHelper.isCrop(unit.getLocation()));
        if (next != null) {
            path = mob.getPathfinder().findPath(next.getLocation());
            if (path != null) {
                currentDestination = next;
                move();
//                System.out.printf("Next point: %s. Locs: %d%n", locToString(next.getLocation()), getCrops().size());
                System.out.println("Harm goal destination changed: " + locToString(path.getFinalPoint()));
            } else {
                System.out.println("Harm goal destination not available");
            }
        } else {
            getCrops().clear();
        }
    }

    private List<LocationMemoryUnit> getCrops() {
        return this.mob.getMemories(MemoryKey.CROP_POINT);
    }

    private void move() {
        mob.getPathfinder().moveTo(path, speed);
    }

    private boolean locationReached() {
        double distance = mob.getLocation().distance(currentDestination.getLocation());
//        System.out.println("Distance: " + distance);
//        System.out.println("Target: " + locToString(currentDestination.getLocation()));
//        System.out.println("Mob: " + locToString(mob.getLocation()));
        return distance < 1.2;
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}
