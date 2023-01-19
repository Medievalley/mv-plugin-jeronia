package org.shrigorevich.ml.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.events.DangerIsGoneEvent;

public class PlayerInteract implements Listener {

    private final StructureService structureService;
    private final NpcService npcService;
    private final TaskService taskService;
    private Villager villager;

    public PlayerInteract(StructureService structureService, NpcService npcService, TaskService taskService) {
        this.structureService = structureService;
        this.npcService = npcService;
        this.taskService = taskService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player p = event.getPlayer();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (event.getHand() == EquipmentSlot.HAND) {
            switch (event.getPlayer().getInventory().getItemInMainHand().getType()) {
                case DIAMOND_SWORD:
                    Bukkit.getPluginManager().callEvent(new DangerIsGoneEvent(entity));
                    break;
                case AIR:
                    showInventory(entity, event.getPlayer());
                default:
                    break;
            }
        }
    }

    private void showInventory(Entity entity, Player player) {
        Inventory inv = Bukkit.createInventory((Villager) entity, 9);
        ItemStack gag = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gagMeta = gag.getItemMeta();
        gagMeta.displayName(Component.text(""));
        gag.setItemMeta(gagMeta);
        for (int i = 1; i < inv.getSize()-1; i++) {
            inv.setItem(i, gag);
        }
        inv.setItem(inv.getSize()-1, new ItemStack(Material.CRIMSON_BUTTON));
//        inv.setContents(((InventoryHolder)entity).getInventory().getContents());
        player.openInventory(inv);
    }
}