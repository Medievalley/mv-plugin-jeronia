package org.shrigorevich.ml.domain.handlers;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.shrigorevich.ml.domain.events.KnockDoorEvent;
import org.shrigorevich.ml.domain.events.StructsDamagedEvent;
import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.domain.structures.StructureService;

import java.util.*;

public class KnockDoorHandler implements Listener {

    private final Map<String, Integer> knocksPerDoor;
    public KnockDoorHandler() {
        this.knocksPerDoor = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnDoorKnocked(KnockDoorEvent event) {
        String key = key(event.getDoorLocation());
        int knocks = 1;
        if (knocksPerDoor.containsKey(key)) {
            knocks += knocksPerDoor.get(key);
        }
        knocksPerDoor.put(key, knocks);

        if (knocks > 2) {
            playSound(event.getDoorLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR);
            event.getDoorLocation().getBlock().breakNaturally();
            knocksPerDoor.remove(key);
        } else {
            playSound(event.getDoorLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR);
        }
    }

    private String key(Location l) {
        return String.format("%d:%d:%d", l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    private void playSound(Location doorLocation, Sound sound) {
        doorLocation.getWorld().playSound(doorLocation, sound, 1, 1);
    }
}
