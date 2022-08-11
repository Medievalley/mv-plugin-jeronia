package org.shrigorevich.ml.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.Structure;

import java.util.Optional;

public class PlayerInteract implements Listener {

    StructureService structureService;
    NpcService npcService;
    public PlayerInteract(StructureService structureService, NpcService npcService) {
        this.structureService = structureService;
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player p = event.getPlayer();

        if(event.getMaterial() == Material.BONE && action.equals(Action.RIGHT_CLICK_BLOCK)) {

            Location l = event.getClickedBlock().getLocation();
            Optional<Structure> s = structureService.getByLocation(l);
            s.ifPresent(structure -> npcService.draftNpc(
                    l.getBlockX(), l.getBlockY() + 1, l.getBlockZ(),
                    structure.getId(), p.getName(), (p::sendMessage)));
        }
        else if(event.getMaterial() == Material.FEATHER && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            structureService.selectStructByLocation(p.getName(), event.getClickedBlock().getLocation(), ((result, msg) -> {
                p.sendMessage(msg);
            }));
        }
        else if (event.getMaterial() == Material.STICK && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            structureService.setCorner(p.getName(), event.getClickedBlock().getLocation());
            for(Location l : structureService.getStructCorners(p.getName())) {
                p.sendMessage(String.format("%d, %d, %d", l.getBlockX() + 5, l.getBlockY() - 77, l.getBlockZ() - 37));
            }
        }
    }
}