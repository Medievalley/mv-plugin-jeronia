package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.shrigorevich.ml.domain.npc.NpcService;

public class EntityInventoryHandler implements Listener {
    private final NpcService npcService;

    public EntityInventoryHandler(NpcService npcService) {
        this.npcService = npcService;
    }
}
