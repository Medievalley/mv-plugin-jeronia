package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        System.out.println(event.getRawSlot());
        if (event.getClickedInventory() != null) {
            InventoryHolder holder = event.getClickedInventory().getHolder();
            if (holder instanceof Villager villager) {
                npcService.getById(villager.getUniqueId()).ifPresent(npc -> {
                    System.out.println("Npc inventory clicked");
                    if (npc.getRole() == NpcRole.WARDEN) {
                        processWarden(event);
//                    event.getView().setCursor(new ItemStack(Material.COBBLESTONE, 50));
                    }
                });
            }
        }
    }

    private void processWarden(InventoryClickEvent event) {

        switch (event.getAction()) {
            case PLACE_ONE:
            case PLACE_ALL:

                break;
            default:
                System.out.println(event.getAction());
                break;
        }
    }

    private void processResources() {

    }
}
