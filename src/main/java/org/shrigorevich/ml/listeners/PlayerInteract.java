package org.shrigorevich.ml.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.shrigorevich.ml.domain.services.IStructureService;

public class PlayerInteract implements Listener {

    IStructureService structureService;
    public PlayerInteract(IStructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player p = event.getPlayer();

        if(event.getMaterial() == Material.FEATHER && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            structureService.selectStructByLocation(p.getName(), event.getClickedBlock().getLocation(), ((result, msg) -> {
                p.sendMessage(msg);
            }));
        }
        else if (event.getMaterial() == Material.STICK && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            structureService.setCorner(p.getName(), event.getClickedBlock().getLocation());
            for(Location l : structureService.getStructCorners(p.getName())) {
                p.sendMessage(String.format("%d, %d, %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
            }
        }
    }
}