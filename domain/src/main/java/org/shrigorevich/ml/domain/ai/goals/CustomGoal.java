package org.shrigorevich.ml.domain.ai.goals;

import net.minecraft.util.Mth;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;

import java.util.List;
import java.util.function.Predicate;

public abstract class CustomGoal {
    protected int reducedTickDelay(int serverTicks) {
        return Mth.positiveCeilDiv(serverTicks, 2);
    }
    protected int adjustedTickDelay(int ticks, boolean requiresUpdateEveryTick) {
        return requiresUpdateEveryTick ? ticks : reducedTickDelay(ticks);
    }

    protected String locToString(Location l) {
        return String.format("x: %s, y: %s, z: %s", l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    protected @Nullable LocationMemoryUnit findClosest(Location target, List<LocationMemoryUnit> units, Predicate<LocationMemoryUnit> filter) {
        double lastDistance = -1.0D;
        LocationMemoryUnit closest = null;
        for (LocationMemoryUnit unit : units) {
            if (filter.test(unit)) {
                double distance = unit.getLocation().distanceSquared(target);
                if (lastDistance == -1.0D || lastDistance > distance) {
                    lastDistance = distance;
                    closest = unit;
                }
            }
        }
        return closest;
    }

    public static boolean isCropReachable(Location l1, Location l2) {
        return l1.getBlockX() == l2.getBlockX() &&
                l1.getBlockZ() == l2.getBlockZ() &&
                Math.abs(l1.getY() - l2.getY()) < 2;
    }
}
