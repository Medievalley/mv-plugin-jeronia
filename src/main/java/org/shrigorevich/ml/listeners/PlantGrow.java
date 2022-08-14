package org.shrigorevich.ml.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.events.StartHarvestEvent;

import java.util.Optional;

public class PlantGrow implements Listener {

    private final StructureService structureService;

    public PlantGrow(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnGrow(BlockGrowEvent event) {
        Block b = event.getBlock();

        if (hasGrown(b)) {
            Ageable plant = (Ageable) b.getBlockData();
            Optional<Structure> structure = structureService.getByLocation(b.getLocation());

            if (structure.isPresent() && structure.get().getType() == StructureType.LORE) {
                LoreStructure loreStructure = (LoreStructure) structure.get();
                System.out.println(String.format("Plant grown (age %d) in struct: %d", plant.getAge(), loreStructure.getId()));

                Optional<Villager> e = loreStructure.getLaborer();
                e.ifPresent(entity -> structureService.getPlugin()
                    .getServer().getPluginManager().callEvent(new StartHarvestEvent(entity, b)));
            }
        }
    }

    private boolean hasGrown(Block b) {
        Ageable plant = (Ageable) b.getBlockData();
        return plant.getAge() >= 3;
    }
}
