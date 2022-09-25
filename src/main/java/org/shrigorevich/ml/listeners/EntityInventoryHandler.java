package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.events.ReplenishStorageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityInventoryHandler implements Listener {
    private final NpcService npcService;
    private final ProjectService projectService;
    private final int resourceSlot = 0;
    private final int buttonSlot = 8;
    private final Map<UUID, HumanEntity> timeoutList;
    public EntityInventoryHandler(NpcService npcService, ProjectService projectService) {
        this.npcService = npcService;
        this.projectService = projectService;
        this.timeoutList = new HashMap<>();
    }
    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder holder = event.getClickedInventory().getHolder();
            if (holder instanceof Villager villager) {
                npcService.getById(villager.getUniqueId()).ifPresent(npc -> {
                    if (npc.getRole() == NpcRole.WARDEN) {
                        if (event.getRawSlot() == buttonSlot) {
                            processResources(event.getClickedInventory(), event.getView());
                            event.setCancelled(true);
                        } else if (event.getRawSlot() != resourceSlot) {
                            event.setCancelled(true);
                        }
                    }
                });
            }
        }
    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Villager villager) {
            npcService.getById(villager.getUniqueId()).ifPresent(npc -> {
                if (npc.getRole() == NpcRole.WARDEN) {
                    ItemStack item = event.getInventory().getContents()[0];
                    if (item != null) {
                        Map<Integer, ItemStack> rest = event.getPlayer().getInventory().addItem(item);
                        if (!rest.isEmpty()) {
                            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), rest.get(0));
                        }
                    }
                }
            });
        }
    }

    private void processResources(Inventory inventory, InventoryView view) {
        ItemStack item = inventory.getItem(resourceSlot);
        if (item != null && !timeoutList.containsKey(view.getPlayer().getUniqueId()) && getResourceValue(item.getType()) > 0) {
            projectService.getCurrent().ifPresent(project -> {
                double resourceNeeded = Math.max(project.getBrokenSize() - projectService.getResources(), 0);
                if (resourceNeeded > 0) {
                    int blockEquivalent = (int) Math.ceil(resourceNeeded / getResourceValue(item.getType()));
                    int countedItems = Math.min(blockEquivalent, item.getAmount());
                    int countedResources = countedItems * getResourceValue(item.getType());
                    int change = item.getAmount()-blockEquivalent;
                    inventory.clear(resourceSlot);
                    if (change >= 0) {
                        item.setAmount(change);
                        Map<Integer, ItemStack> rest = view.getPlayer().getInventory().addItem(item);
                        if (!rest.isEmpty()) {
                            view.setCursor(rest.get(0));
                        }
                        timeoutList.put(view.getPlayer().getUniqueId(), view.getPlayer());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(npcService.getPlugin(), () -> timeoutList.remove(view.getPlayer().getUniqueId()), 100);
                    }
                    Bukkit.getServer().getPluginManager().callEvent(new ReplenishStorageEvent(countedResources));
                }
            });
        }
    }

    private int getResourceValue(Material type) {
        return switch (type) {
            case COBBLESTONE, ACACIA_PLANKS,
                    BIRCH_PLANKS, DARK_OAK_PLANKS, JUNGLE_PLANKS, MANGROVE_PLANKS, OAK_PLANKS -> 1;
            case STONE, ACACIA_LOG, BIRCH_LOG, DARK_OAK_LOG, JUNGLE_LOG, MANGROVE_LOG, OAK_LOG -> 2;
            case IRON_INGOT -> 10;
            case GOLD_INGOT -> 15;
            case DIAMOND -> 20;
            default -> 0;
        };
    }
}
