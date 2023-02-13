package org.shrigorevich.ml.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.mob.events.SpawnRegularMobsEvent;
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

        for (Structure s : structSvc.getStructs(StructureType.REGULAR_ABODE)) {
            regSpawns.addAll(((AbodeStructure)s).getSpawnBlocks());
        }
        this.world = plugin.getServer().getWorld("world"); //TODO: Hardcoded world
    }

    @EventHandler
    public void OnRegularSpawn(SpawnRegularMobsEvent event) {

        List<EntityType> mobTypes = mobSvc.getMobTypesForRegSpawn();
        int allowedQtyPerType = (config.getMaxMobQty() - mobSvc.getCurrentQuantity()) / mobTypes.size() + 1;
        int powerPerType = (getPower() - mobSvc.getCurrentPower()) / mobTypes.size();

        if (config.getMaxMobQty() <= 0 || allowedQtyPerType <= 0 || powerPerType <= 0) return;

        for (EntityType t : mobTypes) {
            int qty = powerPerType / getDefaultMobPower(t);
            int extraQty = qty - allowedQtyPerType;
            if (extraQty > 0) {
                spawnEntities(t, allowedQtyPerType - extraQty, 0);
                spawnEntities(t, extraQty, getDefaultMobPower(t));
            } else {
                spawnEntities(t, qty, 0);
            }
            logger.debug(String.format("Type: %s. Qty: %d", t, qty));
        }
    }

    @EventHandler
    public void OnWaveSpawn(SpawnWaveEvent event) {

    }

    private int getPower() {
        return (int) Math.floor(plugin.getServer().getOnlinePlayers().size() * config.getRegSpawnPlayersFactor()); //TODO: use players factor;
    }

    private void spawnEntities(EntityType type, int qty, int extraPower) {

        for (int i = 0; i < qty; i++) {
            StructBlock b = regSpawns.poll();
            if (b != null) {
                world.spawnEntity(
                    new Location(world, b.getX(), b.getY(), b.getZ()),
                    type, CreatureSpawnEvent.SpawnReason.CUSTOM,
                    (e) -> {
                        if (extraPower > 0) boostEntity(e, extraPower);
                    }
                );
            }
        }
    }

    private void boostEntity(Entity entity, int power) {
        if (entity instanceof Attributable attributable) {
            attributable.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0D);
        } else {
            logger.error(String.format("Cannot upgrade attributes of entity: %s", entity.getType().toString()));
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
