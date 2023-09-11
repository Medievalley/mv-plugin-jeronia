package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.common.MaterialHelper;
import org.shrigorevich.ml.domain.events.KnockDoorEvent;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.ValleyMob;

import java.util.EnumSet;
import java.util.List;

public class ValleyBreakDoorGoal extends ValleyGoal implements Goal<Mob> {

    private final int knockDoorInterval;
    private final double speed;
    private final GoalKey<Mob> key;
    private Pathfinder.PathResult path;
    private LocationMemoryUnit door;
    private final ValleyMob mob;
    private final net.minecraft.world.entity.Mob handle;

    public ValleyBreakDoorGoal(ValleyMob mob) {
        super();
        this.mob = mob;
        this.handle = ((CraftMob) mob.getHandle()).getHandle();
        this.knockDoorInterval = 20;
        this.speed = 1.0D;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("valley", "breakdoorgoal"));
    }

    @Override
    public boolean shouldActivate() {
        return getDoors().size() > 0;
    }

    @Override
    public void start() {
        System.out.println("BreakDoor goal started");
        defineNextDestination();
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
        System.out.println("BreakDoor goal stopped");
    }

    @Override
    public void tick() {
        if (door != null) {
            mob.getHandle().lookAt(door.getLocation());
        }
        if (this.knockDoorInterval > 0 && this.handle.getRandom().nextInt(this.knockDoorInterval) == 0) {
            if (door != null && pathIsDone()){
                if (mob.isDoorReachable(path, door.getLocation())) {
                    if (MaterialHelper.isDoor(door.getLocation())) {
                        callEvent(new KnockDoorEvent(mob, door.getLocation()));
                    } else {
                        //add checkHouse memory unit
                        finalizeDoor();
                    }
                } else {
                    finalizeDoor();
                }

            } else if(door == null || path == null){
                defineNextDestination();
            } else {
                move();
            }
        }
    }

    private void defineNextDestination() {
        LocationMemoryUnit next = findClosest(mob.getLocation(), getDoors(),
                unit -> MaterialHelper.isDoor(unit.getLocation()));
        if (next != null) {
            path = mob.getPathfinder().findPath(next.getLocation());
            if (path != null) {
                door = next;
                move();
                System.out.println("Next door: " + locToString(next.getLocation()));
                System.out.println("Door destination changed: " + locToString(path.getFinalPoint()));
            } else {
                System.out.println("Door destination not available");
            }
        } else {
            getDoors().clear();
        }

    }

    private void finalizeDoor() {
        getDoors().remove(door);
        door = null;
    }
    private void move() {
        mob.getPathfinder().moveTo(path, speed);
    }

    private boolean pathIsDone() {
        return handle.getNavigation().isDone();
    }

    private List<LocationMemoryUnit> getDoors() {
        return this.mob.getMemories(MemoryKey.DOOR_POINT);
    }
    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return this.key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }
}
