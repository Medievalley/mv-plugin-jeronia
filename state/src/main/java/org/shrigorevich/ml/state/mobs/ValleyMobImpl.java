package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.mobs.ValleyMob;
import org.shrigorevich.ml.domain.mobs.MobType;
import org.shrigorevich.ml.domain.mobs.ScanBox;

import java.util.UUID;

abstract class ValleyMobImpl extends MemoryHolderImpl implements ValleyMob {

    private final Mob entity;
    private final net.minecraft.world.entity.Mob handle;
    private final double power;
    private final MobType type;
    private int scanOffsetY = 4;
    private int scanRadiusX = 7;
    private int scanRadiusY = 4;
    private int scanRadiusZ = 7;

    public ValleyMobImpl(Mob entity, double power, MobType type) {
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

    @Override
    public ScanBox getScanBox() {
        return getScanBox(scanOffsetY, scanRadiusX, scanOffsetY, scanRadiusZ);
    }

    @Override
    public ScanBox getScanBox(int offsetY, int rX, int rY, int rZ) {
        return new ScanBoxImpl(this.getLocation(), offsetY, rX, rY, rZ);
    }

    @Override
    public boolean isDoorReachable(Location door) {
        return isDoorReachable(getPathfinder().findPath(door), door);
    }

    @Override
    public boolean isDoorReachable(Pathfinder.PathResult path, Location door) {
        return path != null && path.getFinalPoint() != null && isDoorReachable(path.getFinalPoint(), door);
    }

    private boolean isDoorReachable(Location l1, Location l2) {
        return Math.abs(l1.getX() - l2.getX()) < 1.6D &&
                Math.abs(l1.getZ() - l2.getZ()) < 1.6D &&
                Math.abs(l1.getY() - l2.getY()) < 2;
    }
}
