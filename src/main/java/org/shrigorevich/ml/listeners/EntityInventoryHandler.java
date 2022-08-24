package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.shrigorevich.ml.domain.npc.NpcService;

public class EntityInventoryHandler implements Listener {
    private final NpcService npcService;

    public EntityInventoryHandler(NpcService npcService) {
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnReachLocation(EntityPickupItemEvent event) {
        npcService.getById(event.getEntity().getUniqueId()).ifPresent(npc -> {
            if (isPlantFood(event.getItem().getItemStack().getType())) {
                event.setCancelled(true);
                event.getItem().remove();
            }
        });
    }

    private boolean isPlantFood(Material type) {
        switch (type) {
            case WHEAT:
            case POTATO:
            case CARROT:
                return true;
            default:
                return false;
        }
    }
}
