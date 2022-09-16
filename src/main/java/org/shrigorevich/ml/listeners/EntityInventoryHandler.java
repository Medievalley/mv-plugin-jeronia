package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.project.ProjectService;

public class EntityInventoryHandler implements Listener {
    private final NpcService npcService;
    private final ProjectService projectService;
    public EntityInventoryHandler(NpcService npcService, ProjectService projectService) {
        this.npcService = npcService;
        this.projectService = projectService;
    }
    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder holder = event.getClickedInventory().getHolder();
            if (holder instanceof Villager villager) {
                npcService.getById(villager.getUniqueId()).ifPresent(npc -> {
                    if (npc.getRole() == NpcRole.WARDEN && event.getRawSlot() == 8) {
                        processResources(event.getClickedInventory());
//                    event.getView().setCursor(new ItemStack(Material.COBBLESTONE, 50));
                    }
                });
            }
        }
    }

    private void processResources(Inventory inventory) {
        ItemStack item = inventory.getItem(0);
        if (item != null && getResourceValue(item.getType()) != 0) {
            projectService.getCurrent().ifPresent(project -> {
                double resourceNeeded = Math.max(project.getBrokenSize() - projectService.getStorage().getResources(), 0);
                int blockEquivalent = (int) Math.ceil(resourceNeeded / getResourceValue(item.getType()));
                System.out.println("Resource needed: " + resourceNeeded);
                System.out.println("BlockEquivalent: " + blockEquivalent);
                if (item.getAmount()  <= blockEquivalent) {
                    System.out.println("CountedBlocks value: " + item.getAmount());
                } else {
                    System.out.println("CountedBlocks value: " + blockEquivalent);
                    System.out.println("Change: " + (item.getAmount() - blockEquivalent));
                }
            });

        }
    }

    private int getResourceValue(Material type) {
        switch (type) {
            case COBBLESTONE:
            case ACACIA_PLANKS:
            case BIRCH_PLANKS:
            case DARK_OAK_PLANKS:
            case JUNGLE_PLANKS:
            case MANGROVE_PLANKS:
            case OAK_PLANKS:
                return 1;
            case STONE:
            case ACACIA_LOG:
            case BIRCH_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case MANGROVE_LOG:
            case OAK_LOG:
                return 2;
            case IRON_INGOT:
                return 10;
            case GOLD_INGOT:
                return 15;
            case DIAMOND:
                return 20;
            default:
                return 0;
        }
    }
}
