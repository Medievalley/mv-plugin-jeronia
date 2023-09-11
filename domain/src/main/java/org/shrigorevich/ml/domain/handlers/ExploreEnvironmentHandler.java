package org.shrigorevich.ml.domain.handlers;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.goals.ValleyGoal;
import org.shrigorevich.ml.domain.events.ExploreEnvironmentEvent;
import org.shrigorevich.ml.domain.mobs.ValleyMob;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.structures.Structure;
import org.shrigorevich.ml.domain.structures.StructureService;
import org.shrigorevich.ml.domain.structures.StructureType;
import org.shrigorevich.ml.common.MaterialHelper;

import java.util.List;


public class ExploreEnvironmentHandler implements Listener {

    private final Plugin plugin;
    private final StructureService structService;
    public ExploreEnvironmentHandler (Plugin plugin, StructureService structService) {
        this.plugin = plugin;
        this.structService = structService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void OnExploreRequest(ExploreEnvironmentEvent event) {
        exploreEnvironment(event.getMob());
    }

    private void exploreEnvironment(ValleyMob mob) {
//        System.out.printf("Mob loc: %d %d %d%n", mob.getLocation().getBlockX(), mob.getLocation().getBlockY(), mob.getLocation().getBlockZ());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<Structure> structs = structService.getIntersected(
                    mob.getScanBox(3, 3, 3, 3)); //TODO: move to right place

//            System.out.println("Intersected structs: " + structs.size());
            for (Structure struct : structs) {
                if (struct.getType() == StructureType.AGRONOMIC) {
                    findCrops(mob, struct);
                } else {
                    analyzeStruct(mob, struct);
                }
            }
        });
    }

    private void analyzeStruct(ValleyMob mob, Structure struct) {
        for (Block b : struct.getBlocks()) {
            if (MaterialHelper.isDoor(b)) { //TODO: isReachable
                mob.addMemory(MemoryKey.DOOR_POINT, b.getLocation());
            }
        }
    }

    private void findCrops(ValleyMob mob, Structure struct) {
        for (Block b : struct.getBlocks()) {
            if (MaterialHelper.isCrop(b.getType())) {
                Pathfinder.PathResult path = mob.getPathfinder().findPath(b.getLocation());
                if (path != null && ValleyGoal.isCropReachable(b.getLocation(), mob.getLocation()))
                    mob.addMemory(MemoryKey.CROP_POINT, b.getLocation());
            }
        }
//        System.out.println("Crop size: " + mob.getMemories(MemoryKey.CROP_POINT).size());
    }
}
