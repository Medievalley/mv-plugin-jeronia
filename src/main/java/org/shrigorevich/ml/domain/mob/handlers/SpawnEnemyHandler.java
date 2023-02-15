package org.shrigorevich.ml.domain.mob.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.mob.events.SpawnPressureMobsEvent;
import org.shrigorevich.ml.domain.mob.events.SpawnWaveEvent;
import org.shrigorevich.ml.domain.structure.*;

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

        for (Structure s : this.structSvc.getStructs(StructureType.PRESSURE)) {
            regSpawns.addAll(((AbodeStructure)s).getSpawnBlocks());
        }
        this.world = plugin.getServer().getWorld("world"); //TODO: Hardcoded world
    }

    @EventHandler
    public void OnPressureSpawn(SpawnPressureMobsEvent event) {
        for (EntityType t : mobSvc.getPressurePreset().keySet()) {
            if (getAvailableQty(t) <= 0) return;

            int qty = getPowerToSpawn(t) / getDefaultMobPower(t);
            double powerFactor = (double) qty / getAvailableQty(t);

            if (powerFactor <= 0) {
                spawnEntities(t, qty, 0);
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
                    new Location(world, b.getX(), b.getY(), b.getZ()),
                    type, CreatureSpawnEvent.SpawnReason.CUSTOM,
                    (e) -> {
                        if (powerFactor > 0) boostEntity(e, powerFactor);
                    }
                );
            }
        }
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
}
