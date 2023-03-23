package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.MobGoals;
import net.minecraft.core.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MobType;

import java.util.UUID;

abstract class CustomMobImpl extends MemoryHolderImpl implements CustomMob {

    private final Mob entity;
    private final net.minecraft.world.entity.Mob handle;
    private final double power;
    private final MobType type;

    public CustomMobImpl(Mob entity, double power, MobType type) {
        super();
        this.entity = entity;
        this.power = power;
        this.type = type;
        this.handle = ((CraftMob) entity).getHandle();
    }

    @Override
    public UUID getId() {
        return entity.getUniqueId();
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public MobType getType() {
        return type;
    }

    @Override
    public Pathfinder getPathfinder() {
        return entity.getPathfinder();
    }

    @Override
    public Mob getHandle() {
        return entity;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public void removeVanillaAI() {
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.removeAllGoals(entity);
    }
}
