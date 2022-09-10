package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;
import org.shrigorevich.ml.domain.ai.BuildTask;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.BuildTaskImpl;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.BuildProjectImpl;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.scoreboard.BoardType;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.BuildStartedEvent;
import org.shrigorevich.ml.events.StructsDamagedEvent;
import org.shrigorevich.ml.events.StructsLoadedEvent;
import org.shrigorevich.ml.events.ReplenishStorageEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildProjectHandler implements Listener {
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;
    private final TaskService taskService;
    private final StructureService structureService;

    public BuildProjectHandler(ProjectService projectService, ScoreboardService scoreboardService, NpcService npcService, TaskService taskService, StructureService structureService) {
        this.projectService = projectService;
        this.scoreboardService = scoreboardService;
        this.npcService = npcService;
        this.taskService = taskService;
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStructLoaded(StructsLoadedEvent event) {
        List<LoreStructure> structs = event.getStructures();
        for (LoreStructure s : structs) {
            List<StructBlockModel> blocks = s.getStructBlocks();
            List<StructBlockModel> brokenBlocks = blocks.stream().filter(StructBlockModel::isBroken).collect(Collectors.toList());
            BuildProject project = new BuildProjectImpl(s, blocks.size());
            updateProject(project, brokenBlocks);
            projectService.addProject(project);
        }

        projectService.getCurrent().ifPresent(project -> {
            System.out.printf("Current project: %s. Broken blocks: %d%n", project.getStruct().getName(), project.getBrokenSize());
            updateScoreboard(project);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStructsDamaged(StructsDamagedEvent event) {
        Map<Integer, List<StructBlockModel>> brokenBlocks = event.getBrokenBlocks();

        for (int structId : brokenBlocks.keySet()) {
            structureService.getById(structId).ifPresent(struct -> {
                BuildProject project = projectService.getProject(structId).orElseGet(() -> {
                    List<StructBlockModel> blocks = struct.getStructBlocks();
                    BuildProject newProject = new BuildProjectImpl(struct, blocks.size());
                    projectService.addProject(newProject);
                    return newProject;
                });
                updateProject(project, brokenBlocks.get(structId));
                System.out.printf("Project %d updated", project.getId());
            } );
        }

        projectService.getCurrent().ifPresent(project -> {
            if (brokenBlocks.containsKey(project.getId())) {
                updateScoreboard(project);
                callBuilders(project);
                System.out.printf("Current project: %s. Broken blocks: %d%n", project.getStruct().getName(), project.getBrokenSize());
            }
        });


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBuildStart(BuildStartedEvent event) {
        System.out.println("BuildStartedEvent");
        Entity entity = event.getEntity();
        StructBlockModel block = event.getTask().getBlock();

        entity.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ())
                .setType(Material.valueOf(block.getType()));

        projectService.getProject(block.getStructId()).ifPresent(p -> {
            structureService.getById(block.getStructId()).ifPresent(s -> {
                s.restoreBlock(block);
                taskService.finalize(entity.getUniqueId());
                p.decrementBrokenSize();
            });
        });
        projectService.getCurrent().ifPresent(project -> {
            if (project.getId() == block.getStructId()) {
                updateScoreboard(project);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBuildStart(ReplenishStorageEvent event) {
        projectService.getStorage().updateResources(event.getAmount());
    }

    private void updateProject(BuildProject project, List<StructBlockModel> brokenBlocks) {
        for (StructBlockModel block : brokenBlocks) {
            project.addPlannedBlock(block);
        }
    }

    private void updateScoreboard(BuildProject project) {
        Scoreboard board = scoreboardService.getScoreboard(BoardType.PROJECT);
        Objective curObjective = board.getObjective(BoardType.PROJECT.toString());

        if (curObjective != null) {
            curObjective.unregister();
        }
        Objective objective = scoreboardService.createObjective(
                BoardType.PROJECT, DisplaySlot.SIDEBAR,
                "Project: " + project.getStruct().getName()
        );

        int resourceNeeded = project.getBrokenSize() - projectService.getStorage().getResources();
        objective.getScore("Resources needed:").setScore(Math.max(resourceNeeded, 0));
        objective.getScore("Health:").setScore(project.getHealthPercent());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(board);
        }
    }


    private void callBuilders(BuildProject project) {
        npcService.getNpcByRole(NpcRole.BUILDER).forEach(npc -> {
            while (!project.isPlanEmpty()) {
                StructBlockModel block = project.getPlannedBlock();
                BuildTask task = new BuildTaskImpl(
                        npcService.getPlugin(),
                        (Villager) Bukkit.getEntity(npc.getEntityId()),
                        block,
                        Bukkit.getWorld(npc.getWorld())
                                .getBlockAt(block.getX(), block.getY(), block.getZ()).getLocation());
                taskService.add(task);
            }
        });
    }
}
