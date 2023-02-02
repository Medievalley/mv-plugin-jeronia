package org.shrigorevich.ml.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.mob.events.SpawnRegularMobsEvent;
import org.shrigorevich.ml.domain.mob.events.SpawnWaveEvent;
import org.shrigorevich.ml.domain.structure.StructureService;

import java.util.Comparator;
import java.util.List;

public class SpawnEnemyHandler implements Listener {
    private final StructureService structSvc;
    private final MobService mobSvc;
    private final MlConfiguration config;
    private final MlPlugin plugin;

    public SpawnEnemyHandler(StructureService structSvc, MobService mobSvc, MlConfiguration config, MlPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
        this.structSvc = structSvc;
        this.mobSvc = mobSvc;
    }

    @EventHandler
    public void OnRegularSpawn(SpawnRegularMobsEvent event) {
        int availableQty = config.getMaxMobQty() - mobSvc.getCurrentQuantity();
        int powerToSpawn = getPower() - mobSvc.getCurrentPower();
        List<EntityType> mobTypes = mobSvc.getMobTypesForRegSpawn();
        int quantityToSpawn = powerToSpawn / getMinMobPower();


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
