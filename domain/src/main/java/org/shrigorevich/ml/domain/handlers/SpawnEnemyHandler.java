package org.shrigorevich.ml.domain.handlers;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.shrigorevich.ml.common.config.MlConfiguration;
import org.shrigorevich.ml.domain.MlPlugin;
import org.shrigorevich.ml.domain.events.SpawnPressureMobsEvent;
import org.shrigorevich.ml.domain.events.SpawnWaveEvent;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.mobs.MobType;
import org.shrigorevich.ml.domain.mobs.PointMemoryUnitImpl;
import org.shrigorevich.ml.domain.structures.*;

import java.util.*;

public class SpawnEnemyHandler implements Listener {
    private final StructureService structSvc;
    private final MobService mobSvc;
    private final MlConfiguration config;
    private final MlPlugin plugin;
    private final Logger logger;
    private final Queue<StructBlock> regSpawns;
    private final World world;

    public SpawnEnemyHandler(StructureService structSvc, MobService mobSvc, MlConfiguration config, MlPlugin plugin) {

        this.config = config;
        this.plugin = plugin;
        this.structSvc = structSvc;
        this.mobSvc = mobSvc;
        this.logger = LogManager.getLogger("SpawnEnemyHandler");
        this.regSpawns = new LinkedList<>();
        this.world = plugin.getServer().getWorld("world"); //TODO: Hardcoded world
    }

    //TODO: remove comments
    @EventHandler
    public void OnPressureSpawn(SpawnPressureMobsEvent event) {
//        double pf = config.getPressurePlayersFactor();
//        int pi =config.getPressureInterval();
//        int q = config.getMaxMobQty();
        for (Structure s : this.structSvc.getStructs(StructureType.PRESSURE)) {
            this.regSpawns.addAll(((AbodeStructure)s).getSpawnBlocks());
        }
        for (EntityType t : mobSvc.getPressurePreset().keySet()) {
            if (getAvailableQty(t) <= 0) return;
//            int availableQty = getAvailableQty(t);
//            int powerToSpawn = getPowerToSpawn(t);
//            int defMobPower = getDefaultMobPower(t);
            int qty = getPowerToSpawn(t) / getDefaultMobPower(t);
            double powerFactor = (double) qty / getAvailableQty(t);

            if (powerFactor <= 1) {
                spawnEntities(t, qty, powerFactor);
            } else {
                spawnEntities(t, getAvailableQty(t), powerFactor);
            }
        }
    }

    @EventHandler
    public void OnWaveSpawn(SpawnWaveEvent event) {

    }

    private int getPowerToSpawn() {
        return (int) Math.floor(plugin.getServer().getOnlinePlayers().size() * config.getPressurePlayersFactor());
    }

    private int getPowerToSpawn(EntityType type) {
        return (int) Math.ceil(
            (getPowerToSpawn() - mobSvc.getCurrentPower()) * mobSvc.getPressurePreset().get(type).getPowerPercent());
    }

    private int getAvailableQty(EntityType type) {
        return (int) Math.ceil(
            (config.getMaxMobQty() - mobSvc.getCurrentQuantity()) * mobSvc.getPressurePreset().get(type).getQtyPercent());
    }


    private void spawnEntities(EntityType type, int qty, double powerFactor) {
        for (int i = 0; i < qty; i++) {
            StructBlock b = regSpawns.poll();
            if (b != null) {
                world.spawnEntity(
                    new Location(world, b.getX(), b.getY()+1, b.getZ()),
                    type, CreatureSpawnEvent.SpawnReason.CUSTOM,
                    (e -> setupCustomMob(e, powerFactor))
                );
                regSpawns.add(b);
            }
        }
    }

    private void setupCustomMob(Entity e, double powerFactor) {
        Mob mob = (Mob) e;
        removeAI(mob);
        int defPower = getDefaultMobPower(e.getType());
        CustomMob customMob = mobSvc.createMob(
            (Mob) e,
            getMobType(e.getType()),
            Math.max(defPower, defPower * powerFactor));

        if (powerFactor > 1) {
            boostEntity(e, powerFactor);
        }

        Structure nearest = structSvc
            .getNearest(e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ());

        //TODO: It may not be possible to lay a route to the structure center
        customMob.addMemory(new PointMemoryUnitImpl(nearest.getCenter()));
        mobSvc.addMob(customMob);
    }

    private void removeAI(Mob mob) {
        mob.getPathfinder().setCanOpenDoors(true);
        mob.getPathfinder().setCanPassDoors(true);
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.removeAllGoals(mob);
    }

    private void boostEntity(Entity entity, double powerFactor) {
        if (entity instanceof Attributable attr) {
            try {
                if (AttrHelper.hasDamageAttr(attr)) {
                    AttrHelper.boostDamage(attr, powerFactor);
                }
                if (AttrHelper.hasHealthAttr(attr)) {
                    AttrHelper.boostMaxHealth(attr, powerFactor);
                }
                if (AttrHelper.hasSpeedAttr(attr)) {
                    AttrHelper.boostSpeed(attr, powerFactor);
                }
            } catch (NullPointerException ex) {
                logger.error(String.format("Entity %s does not have attribute", entity.getType()));
            }
        } else {
            logger.error(String.format("Cannot upgrade attributes of entity: %s", entity.getType()));
        }
    }

    //TODO: get from config
    public int getDefaultMobPower(EntityType type) {
        switch (type) {
            case ZOMBIE, SPIDER, SKELETON -> {
                return 1;
            }
            case CREEPER -> {
                return 2;
            }
            default -> {
                return 0;
            }
        }
    }

    //TODO: get from DB
    private MobType getMobType(EntityType entityType) {
        switch (entityType) {
            case ZOMBIE -> {
                return MobType.PRESSURE_ZOMBIE;
            }
            case SKELETON -> {
                return MobType.PRESSURE_SKELETON;
            }
            case CREEPER -> {
                return MobType.PRESSURE_CREEPER;
            }
            case SPIDER -> {
                return MobType.PRESSURE_SPIDER;
            }
            default -> throw new IllegalArgumentException(String.format("Entity type: %s has no mapping to a mob type", entityType));
        }
    }
}
