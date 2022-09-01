package org.shrigorevich.ml.listeners;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.shrigorevich.ml.domain.structure.StructureService;

public class BlockExplode implements Listener {

    StructureService structureService;
    public BlockExplode(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(BlockExplodeEvent event) {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(EntityExplodeEvent event) {
        structureService.processExplodedBlocksAsync(event.blockList());
    }
}