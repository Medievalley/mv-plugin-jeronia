package org.shrigorevich.ml.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.SafeLocImpl;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.events.DangerIsGoneEvent;

import java.util.Optional;

public class PlayerInteract implements Listener {

    private final StructureService structureService;
    private final NpcService npcService;
    private final TaskService taskService;
    public PlayerInteract(StructureService structureService, NpcService npcService, TaskService taskService) {
        this.structureService = structureService;
        this.npcService = npcService;
        this.taskService = taskService;
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
                case DIAMOND_AXE:
                    regSafeLocation(event.getClickedBlock().getLocation());
                default:
                    break;
            }
        } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            switch (event.getMaterial()) {
                case BONE:
                    draftNpcSetSpawn(event);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (event.getHand() == EquipmentSlot.HAND) {
            switch (event.getPlayer().getInventory().getItemInMainHand().getType()) {
                case DIAMOND_SWORD:
                    Bukkit.getPluginManager().callEvent(new DangerIsGoneEvent(entity));
                    break;
                case DIAMOND_HOE:
                    showInventory(entity, event.getPlayer());
                default:
                    break;
            }
        }
    }

    private void showBlockType(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        event.getPlayer().sendMessage(String.format("Block type: %s", b.getType()));

        if (b.getBlockData() instanceof Ageable) {
            event.getPlayer().sendMessage(String.format("Age: %s", ((Ageable) b.getBlockData()).getAge()));
        }
    }
    private void draftNpc(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Location l = event.getClickedBlock().getLocation();

        Optional<Structure> s = structureService.getByLocation(l);

        s.ifPresent(structure -> npcService.draftNpc(
                l.getBlockX(), l.getBlockY() + 1, l.getBlockZ(),
                structure.getId(), p.getName(), (p::sendMessage)));
    }
    private void draftNpcSetSpawn(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Location l = event.getClickedBlock().getLocation().clone().add(0, 1, 0);
        npcService.draftNpcSetSpawn(l.getBlockX(), l.getBlockY(), l.getBlockZ(), p.getName(), (p::sendMessage));
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
    private void regSafeLocation(Location l) {
        if (l != null) {
            structureService.getByLocation(l).ifPresent(struct -> {
                npcService.regSafeLoc(new SafeLocImpl(l.clone().add(0, 1, 0), struct.getId()));
            });
        }
    }
    private void showInventory(Entity entity, Player player) {
        Inventory inv = Bukkit.createInventory(player, 9);
        inv.setContents(((InventoryHolder)entity).getInventory().getContents());
        player.openInventory(inv);
    }
}