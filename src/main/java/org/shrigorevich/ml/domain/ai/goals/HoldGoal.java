package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HoldGoal extends BaseGoal implements Goal<Mob> {
    private final Location target;
    private int stuckTicks = 0;
    private int cooldown = 0;
    private Location stuckLocation;
    private Location tempLocation;
    private Location currentLocation;


    public HoldGoal(Plugin plugin, Mob mob, Location target) {
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
        currentLocation = target;
        move();
    }

    @Override
    public void stop() {
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        cooldown+=1;
        if (cooldown == 5) {
            cooldown = 0;
            defineCurrentLocation();
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
                int index = findIndex(path.getPoints(), getMob().getLocation());
                if (index != -1 && index+1 < path.getPoints().size()) {
                    Location loc = path.getPoints().get(index+1);
                    printLocation(loc, "Next point");
                    if (isDoor(loc.getBlock().getType())) {
                        Door door = (Door) loc.getBlock().getBlockData();
                        if (!door.isOpen()) {
                            System.out.println("Door opened");
                            door.setOpen(true);
                            loc.getBlock().setBlockData(door);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                                door.setOpen(false);
                                loc.getBlock().setBlockData(door);
                            }, 20);
                        }
                    } else {
                        tempLocation = loc;
                    }
                }
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

    private void defineCurrentLocation() {
        if (tempLocation == null) {
            currentLocation = target;
        } else {
            if (Utils.isLocationsEquals(tempLocation, getMob().getLocation())) {
                tempLocation = null;
                currentLocation = target;
            } else {
                currentLocation = tempLocation;
            }
        }
    }
    private int findIndex(List<Location> list, Location l) {
        if (list == null || l == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Utils.isLocationsEquals(l, list.get(i))) {
                return i;
            }
        }
        return -1;
    };

    private boolean isDoor(Material type) {
        switch (type) {
            case SPRUCE_DOOR:
            case OAK_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case BIRCH_DOOR:
            case CRIMSON_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case MANGROVE_DOOR:
            case WARPED_DOOR:
                return true;
            default:
                return false;
        }
    }

    private void move() {
        getMob().getPathfinder().moveTo(currentLocation, 0.7D);
    };
}