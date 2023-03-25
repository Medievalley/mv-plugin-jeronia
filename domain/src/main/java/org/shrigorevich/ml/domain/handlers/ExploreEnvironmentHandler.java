package org.shrigorevich.ml.domain.handlers;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.shrigorevich.ml.common.CuboidIterator;
import org.shrigorevich.ml.domain.events.ExploreEnvironmentEvent;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.structures.StructureService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExploreEnvironmentHandler implements Listener {

    private final StructureService structService;
    public ExploreEnvironmentHandler (StructureService structService) {
        this.structService = structService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void OnExploreRequest(ExploreEnvironmentEvent event) {
        exploreEnvironment(event.getMob());
    }

    private void exploreEnvironment(CustomMob mob) {
//        System.out.printf("Mob loc: %d %d %d%n", mob.getLocation().getBlockX(), mob.getLocation().getBlockY(), mob.getLocation().getBlockZ());
        Iterator<Block> iterator = new CuboidIterator(mob.getLocation(),3, 5, 3, 5);
        List<Block> blocks = new ArrayList<>();
        while (iterator.hasNext())
            blocks.add(iterator.next());

//        System.out.println("Size: " + copy.size());
    }
}
