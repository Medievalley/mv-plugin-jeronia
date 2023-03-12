package org.shrigorevich.ml.domain.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.HoldSpawnTask;
import org.shrigorevich.ml.domain.events.CustomSpawnEvent;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.structures.StructureService;

import java.util.Optional;

public class CustomSpawn implements Listener {
    private final TaskService taskService;
    private final NpcService npcService;
    private final StructureService structService;
    private final MobService mobService;
    private final Logger logger;

    public CustomSpawn(TaskService taskService, NpcService npcService, StructureService structureService, MobService mobService) {
        this.taskService = taskService;
        this.npcService = npcService;
        this.structService = structureService;
        this.mobService = mobService;
        this.logger = LogManager.getLogger("CustomSpawn");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnCustomSpawn(CustomSpawnEvent event) {
        if (event.getEntity() instanceof Villager villager) {
            processVillager(villager);
        }
    }

    private void processVillager(Villager villager) {
        Optional<StructNpc> npc = npcService.getById(villager.getUniqueId());
        if (npc.isPresent()) {
            taskService.setDefaultAI(villager);
            setTask(npc.get(), villager);
            logger.debug(String.format("Custom spawned: %d, %s, %s%n",
                    npc.get().getId(), npc.get().isAlive(), npc.get().getRole()));

            switch (npc.get().getRole()) {
                case WARDEN, BUILDER -> {}
                default -> {
                }
            }
        }
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
}
