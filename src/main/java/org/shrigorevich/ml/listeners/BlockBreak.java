package org.shrigorevich.ml.listeners;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.sun.tools.javac.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
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
        structureService.processExplodedBlocksAsync(new ArrayList<>(Arrays.asList(b)));
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockDestroyEvent event) {
        Block b = event.getBlock();
        System.out.println("Destroyed block: " + b.getType());
        structureService.processExplodedBlocksAsync(new ArrayList<>(Arrays.asList(b)));
    }


//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntityBreakDoorEvent event) {
//        //TODO: Handle event
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntitySpawnEvent event) {
//
//        Entity e = event.getEntity();
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntitySpellCastEvent event) {
//        //TODO: Check to possibility of creating custom spells
//    }
}
