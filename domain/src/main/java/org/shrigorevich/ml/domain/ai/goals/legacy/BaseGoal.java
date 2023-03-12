package org.shrigorevich.ml.domain.ai.goals.legacy;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.GoalKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.goals.ActionKey;

import java.util.List;

public abstract class BaseGoal {

    private final GoalKey<Mob> key;
    private boolean achieved;
    private final Mob mob;
    private final Plugin plugin;
    private Location tempLocation;
    private Location currentLocation;
    private final Location target;
    private Event onAchievedEvent;
    private int stuckTicks = 0;
    private int ticks = 0;

    private Location stuckLocation;

    public BaseGoal(Mob mob, Location target, Event event, Plugin plugin, ActionKey key) {
        this(mob, target, plugin, key);
        this.onAchievedEvent = event;
    }

    public BaseGoal(Mob mob, Location target, Plugin plugin, ActionKey key) {
        this.key = GoalKey.of(Mob.class, new NamespacedKey(plugin, key.toString()));
        this.achieved = false;
        this.mob = mob;
        this.plugin = plugin;
        this.target = target;
        this.currentLocation = target;
        this.stuckLocation = getMob().getLocation();
    }

    protected boolean isAchieved() {
        return achieved;
    }

    protected void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    protected Mob getMob() {
        return mob;
    }

    protected Plugin getPlugin() {
        return plugin;
    }

    protected GoalKey<Mob> getGoalKey() {
        return key;
    }

    protected void defineCurrentLocation() {
        if (tempLocation == null) {
            currentLocation = target;
        } else {
            if (distanceSquared(tempLocation, getMob().getLocation())) {
                tempLocation = null;
                currentLocation = target;
            } else {
                currentLocation = tempLocation;
            }
        }
    }
    protected int findIndex(List<Location> list, Location l) {
        if (list == null || l == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (areLocsEquals(l, list.get(i))) {
                return i;
            }
        }
        return -1;
    };

    protected void move(double speed) {
        getMob().getPathfinder().moveTo(currentLocation, speed);
    };


    protected void processStuck() {
        Pathfinder.PathResult path = getMob().getPathfinder().findPath(target);
        if (path != null && path.getNextPoint() != null) {
            int index = findIndex(path.getPoints(), getMob().getLocation());
            if (index != -1 && index+1 < path.getPoints().size()) {
                Location nextLoc = path.getPoints().get(index+1);
//                printLocation(nextLoc, "Next point");
                if (isDoor(nextLoc.getBlock().getType())) {
                    openDoor(nextLoc.getBlock());
                } else if (isDoor(getMobLocation().getBlock().getType())) {
                    openDoor(getMobLocation().getBlock());
                } else {
                    setTempLocation(nextLoc);
                }
            }
        }
    }

    protected void setTempLocation(Location location) {
        this.tempLocation = location;
    }

    protected void updateStuckTicks() {
        if (distanceSquared(getMob().getLocation(), stuckLocation)) {
            this.stuckTicks+=1;
        } else {
            resetStuckTicks();
            stuckLocation = getMob().getLocation();
        }
    }

    protected int getStuckTicks() {
        return stuckTicks;
    }
    protected Location getTarget() {
        return target;
    }

    protected void resetStuckTicks() {
        this.stuckTicks = 0;
    }

    protected void printLocation(Location l, String name) {
        System.out.printf("%s: %d %d %d%n", name, l.getBlockX(), l.getBlockY(), l.getBlockZ());
    };

    protected Location getMobLocation() {
        return getMob().getLocation();
    }

    protected void openDoor(Block block) {
        Door door = (Door) block.getBlockData();
        System.out.println("Door opened");
        door.setOpen(true);
        block.setBlockData(door);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            door.setOpen(false);
            block.setBlockData(door);
        }, 20);
    }

    protected boolean isDoor(Material type) {
        return switch (type) {
            case SPRUCE_DOOR, OAK_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, BIRCH_DOOR, CRIMSON_DOOR, IRON_DOOR,
                    JUNGLE_DOOR, MANGROVE_DOOR, WARPED_DOOR -> true;
            default -> false;
        };
    }

    protected void defaultTick() {
        ticks+=1;
        updateStuckTicks();

        if (ticks == 5) {
            ticks = 0;
            defineCurrentLocation();
            move(0.7D);
            if (!isAchieved() && distanceSquared(getMobLocation(), getTarget())) {
                setAchieved(true);
                if (onAchievedEvent != null) {
                    getPlugin().getServer().getPluginManager()
                            .callEvent(onAchievedEvent);
                }
            }
        }

        if (getStuckTicks() == 20) {
            processStuck();
        } else if (getStuckTicks() == 45) {
            getMob().teleport(currentLocation);
        } else if (getStuckTicks() > 60) {
            resetStuckTicks();
        }
    }

    private boolean areLocsEquals(Location l1, Location l2) {
        return l1.getBlockX() == l2.getBlockX() &&
                l1.getBlockY() == l2.getBlockY() &&
                l1.getBlockZ() == l2.getBlockZ();
    }

    public boolean distanceSquared(Location l1, Location l2) {
        return l1.distanceSquared(l2) < 1;
    }
}
