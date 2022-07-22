package org.shrigorevich.ml.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.shrigorevich.ml.domain.services.IStructureService;

public class BlockExplode implements Listener {

    IStructureService structureService;
    public BlockExplode(IStructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(BlockExplodeEvent event) {
        System.out.println("Blocks exploded: " + event.blockList().size());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(EntityExplodeEvent event) {

        if (event.getEntity() instanceof TNTPrimed) { //TODO: Implement logic

        }

        System.out.println("Blocks exploded: " + event.blockList().size());
        structureService.processExplodedBlocksAsync(event.blockList());
    }
}