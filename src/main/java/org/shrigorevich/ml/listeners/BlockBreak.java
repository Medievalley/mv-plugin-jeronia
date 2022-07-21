package org.shrigorevich.ml.listeners;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.shrigorevich.ml.domain.services.IStructureService;

public class BlockBreak implements Listener {
    IStructureService structureService;
    public BlockBreak(IStructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockBreakEvent event) {

        Player p = event.getPlayer();
        Block b = event.getBlock();
        p.sendMessage("Block broken");
        System.out.println(b.getType() + " " + p.getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void DoorBrokenByEntity(EntityBreakDoorEvent event) {
        //TODO: Handle event
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void DoorBrokenByEntity(EntitySpawnEvent event) {

        Entity e = event.getEntity();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void DoorBrokenByEntity(EntitySpellCastEvent event) {
        //TODO: Check to possibility of creating custom spells
    }
}
