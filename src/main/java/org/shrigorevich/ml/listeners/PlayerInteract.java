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

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            switch (event.getMaterial()) {
                case BONE:
                    draftNpc(event);
                    break;
                case FEATHER:
                    selectStructByLocation(event);
                    break;
                case STICK:
                    setStructCorner(event);
                    break;
                case COAL:
                    showBlockType(event);
                default:
                    break;
            }
        }
    }

    private void showBlockType(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(String.format("Block type: %s", event.getClickedBlock().getType()));
    }
    private void draftNpc(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Location l = event.getClickedBlock().getLocation();

        Optional<Structure> s = structureService.getByLocation(l);

        s.ifPresent(structure -> npcService.draftNpc(
                l.getBlockX(), l.getBlockY() + 1, l.getBlockZ(),
                structure.getId(), p.getName(), (p::sendMessage)));
    }

    private void selectStructByLocation(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        structureService.selectStructByLocation(p.getName(), event.getClickedBlock().getLocation(), ((result, msg) -> {
            p.sendMessage(msg);
        }));
    }

    private void setStructCorner(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        structureService.setCorner(p.getName(), event.getClickedBlock().getLocation());
        for(Location l : structureService.getStructCorners(p.getName())) {
            p.sendMessage(String.format("%d, %d, %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }

}