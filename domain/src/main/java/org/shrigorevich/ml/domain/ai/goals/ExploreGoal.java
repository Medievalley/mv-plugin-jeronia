package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.events.ExploreEnvironmentEvent;
import org.shrigorevich.ml.domain.mobs.ValleyMob;

public abstract class ExploreGoal extends ValleyGoal implements Goal<Mob> {

    private final ValleyMob mob;
    private final int exploreInterval;
    private final net.minecraft.world.entity.Mob handle;
    public ExploreGoal(ValleyMob mob, int exploreInterval) {
        this.mob = mob;
        this.handle = ((CraftMob) mob.getHandle()).getHandle();
        this.exploreInterval = exploreInterval;
    }
    @Override
    public void start() {
        System.out.println("Explore started");
    }

    @Override
    public void stop() {
        System.out.println("Explore stopped started");
    }

    @Override
    public void tick() {
        if (this.exploreInterval > 0 && this.handle.getRandom().nextInt(this.exploreInterval) == 0) {
            Bukkit.getServer().getPluginManager().callEvent(new ExploreEnvironmentEvent(this.mob));
        }
    }
}
