package org.shrigorevich.ml.handlers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.FoodStructure;
import org.shrigorevich.ml.domain.structure.events.StructPlantGrownEvent;

import java.util.Optional;

@Deprecated
public class PlantGrow implements Listener {

    private final StructureService structureService;

    public PlantGrow(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnGrow(BlockGrowEvent event) {

        Block b = event.getBlock();
        Location l = b.getLocation();

//        structureService.getByLocation(b.getLocation()).ifPresent((struct) -> {
//            if (isFullyGrown(b) && struct.getType() == StructureType.AGRONOMIC) {
//                FoodStructure foodStructure = (FoodStructure) struct;
//                Optional<Villager> e = foodStructure.getLaborer();
//                e.ifPresent(entity -> structureService.getPlugin()
//                        .getServer().getPluginManager().callEvent(new StructPlantGrownEvent(entity, b)));
//            }
//        });

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnFertilize(BlockFertilizeEvent event) {
        Block b = event.getBlock();
    }

    private boolean isFullyGrown(Block b) {
        Ageable plant = (Ageable) b.getBlockData();
        return plant.getAge() >= 4;
    }
}
