package org.shrigorevich.ml.domain.ai.goals;

import net.minecraft.util.Mth;
import org.bukkit.Location;

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
}
