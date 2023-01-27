package org.shrigorevich.ml.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.HarvestTask;
import org.shrigorevich.ml.domain.ai.tasks.HoldSpawnTask;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.contracts.StructNpc;
import org.shrigorevich.ml.domain.structure.FoodStructure;
import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.events.CustomSpawnEvent;

import java.util.List;
import java.util.Optional;

import static org.shrigorevich.ml.common.Utils.isStructPlant;

public class CustomSpawn implements Listener {
    private final TaskService taskService;
    private final NpcService npcService;
    private final StructureService structService;
    private final Logger logger;

    public CustomSpawn(TaskService taskService, NpcService npcService, StructureService structureService) {
        this.taskService = taskService;
        this.npcService = npcService;
        this.structService = structureService;
        this.logger = LogManager.getLogger("CustomSpawn");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnCustomSpawn(CustomSpawnEvent event) {
        if (event.getEntity() instanceof Villager entity) {
            Optional<StructNpc> npc = npcService.getById(entity.getUniqueId());
            if (npc.isPresent()) {
                taskService.setDefaultAI(entity);
                setTask(npc.get(), entity);
                logger.debug(String.format("Custom spawned: %d, %s, %s%n",
                        npc.get().getId(), npc.get().isAlive(), npc.get().getRole()));

                switch (npc.get().getRole()) {
                    case HARVESTER -> assignToStruct(npc.get(), entity);
                    case WARDEN, BUILDER,
                    default -> {
                    }
                }
            }
        }
    }

    //TODO: refactor StructureType logic
    private void assignToStruct(StructNpc npc, Villager entity) {
        structService.getStruct(npc.getStructId()).ifPresent(s -> {
            if(s instanceof FoodStructure ls) {
                ((FoodStructure) s).setLaborer(entity);
                if (npc.getRole() == NpcRole.HARVESTER) {
                    scanStructForTasks(ls, entity);
                }
            }
        });
    }

    private void setTask(StructNpc npc, Mob entity) {
        Location location = new Location(entity.getWorld(), npc.getX(), npc.getY(), npc.getZ());
        taskService.add(
            new HoldSpawnTask(
                taskService.getPlugin(),
                entity, location
            )
        );
    }

    private void scanStructForTasks(FoodStructure struct, Villager entity) {
        List<StructBlock> structBlocks = struct.getStructBlocks();
        structBlocks.forEach(b -> {
            Block wBlock = struct.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ());
            if (isStructPlant(wBlock.getType())) {
                Ageable plant = (Ageable) wBlock.getBlockData();
                if (plant.getAge() == plant.getMaximumAge() ) { //TODO: get from config
                    taskService.add(
                        new HarvestTask(
                            taskService.getPlugin(),
                            entity, wBlock.getLocation()
                        )
                    );
                }
            }
        });
    }
}
