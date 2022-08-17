package org.shrigorevich.ml.listeners;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.shrigorevich.ml.domain.structure.StructureService;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockBreak implements Listener {
    StructureService structureService;
    public BlockBreak(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockBreakEvent event) {
//        System.out.printf("Broken block type: %s%n", event.getBlock().getType());
        //TODO: handle event
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockDestroyEvent event) {
        System.out.printf("Broken block type: %s%n", event.getBlock().getType());
        Block b = event.getBlock();
        structureService.processExplodedBlocksAsync(new ArrayList<>(Arrays.asList(b)));
    }

//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntitySpellCastEvent event) {
//        //TODO: Check to possibility of creating custom spells
//    }
}
