package org.shrigorevich.ml.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    public SpawnEnemyHandler(StructureService structSvc, MobService mobSvc, MlConfiguration config, MlPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
        this.structSvc = structSvc;
        this.mobSvc = mobSvc;
        this.logger = LogManager.getLogger("SpawnEnemyHandler");
    }

    @EventHandler
    public void OnRegularSpawn(SpawnRegularMobsEvent event) {
        int qtyPerType = (config.getMaxMobQty() - mobSvc.getCurrentQuantity()) / mobSvc.getMobTypesForRegSpawn().size();
        int powerPerType = (getPower() - mobSvc.getCurrentPower()) / mobSvc.getMobTypesForRegSpawn().size();

        Map<EntityType, Integer> qtyToSpawnPerType = new HashMap<>();
        for (EntityType t : mobSvc.getMobTypesForRegSpawn()) {
            qtyToSpawnPerType.put(t, Math.min(powerPerType / mobSvc.getMobPower(t), qtyPerType));
            logger.debug(String.format("Type: %s. Qty: %d", t.toString(), qtyToSpawnPerType.get(t)));
        }
        Queue<StructBlock> spawns = new LinkedList<>();
        for (Structure s : structSvc.getStructs(StructureType.REGULAR_ABODE)) {
            spawns.addAll(((AbodeStructure)s).getSpawnBlocks());
        }

    }

    @EventHandler
    public void OnWaveSpawn(SpawnWaveEvent event) {

    }

    private int getMinMobPower() {
        return mobSvc.getMobTypesForRegSpawn().stream().min(Comparator.comparingInt(mobSvc::getMobPower))
            .map(mobSvc::getMobPower).orElse(1); //TODO: hardcoded
    }
    private int getPower() {
        return (int) Math.floor(plugin.getServer().getOnlinePlayers().size() * config.getRegSpawnPlayersFactor()); //TODO: use players factor;
    }
}
