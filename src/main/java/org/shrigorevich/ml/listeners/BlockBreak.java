package org.shrigorevich.ml.listeners;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.shrigorevich.ml.domain.services.IStructureService;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockBreak implements Listener {
    IStructureService structureService;
    public BlockBreak(IStructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockBreakEvent event) {
        Block b = event.getBlock();
        //structureService.processExplodedBlocksAsync(new ArrayList<>(Arrays.asList(b)));
        //TODO: handle event
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockDestroyEvent event) {
        Block b = event.getBlock();
        System.out.println("Destroyed block: " + b.getType());
        structureService.processExplodedBlocksAsync(new ArrayList<>(Arrays.asList(b)));
    }

//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntitySpellCastEvent event) {
//        //TODO: Check to possibility of creating custom spells
//    }
}
