package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.BuildTask;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.BuildTaskImpl;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.npc.StructNpc;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.BuildProjectImpl;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.project.Storage;
import org.shrigorevich.ml.domain.scoreboard.BoardType;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.*;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class BuildProjectHandler implements Listener {
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;
    private final TaskService taskService;
    private final StructureService structureService;

    public BuildProjectHandler(
            ProjectService projectService, ScoreboardService scoreboardService,
            NpcService npcService, TaskService taskService,
            StructureService structureService
    ) {
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
            addPlannedBlocks(project, brokenBlocks);
            projectService.addProject(project);
            System.out.println("Project loaded: " + project.getId());
        }
        projectService.getCurrent().ifPresent(project -> {
            System.out.printf("Current project: %s. Broken blocks: %d%n", project.getStruct().getName(), project.getBrokenSize());
            scoreboardService.updateScoreboard(project, projectService.getStorage());
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
                addPlannedBlocks(project, brokenBlocks.get(structId));
            });
        }

        projectService.getCurrent().ifPresent(project -> {
            if (brokenBlocks.containsKey(project.getId())) {
                callBuilders(project);
                scoreboardService.updateScoreboard(project, projectService.getStorage());
                System.out.printf("Current project: %s. Storage: %d%n", project.getStruct().getName(), projectService.getStorage().getResources());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBuildStart(BuildStartedEvent event) {
        Entity entity = event.getEntity();
        StructBlockModel block = event.getTask().getBlock();

        projectService.getProject(block.getStructId()).ifPresent(p -> {
            p.restoreBlock(block);
            taskService.finalize(entity.getUniqueId());
            if (p.getBrokenSize() == 0) {
                projectService.finalizeProject(p);
            }
        });
        updateProjectScoreboard();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnResourceSupplied(ReplenishStorageEvent event) {
        projectService.getStorage().updateResources(event.getAmount());
        projectService.getCurrent().ifPresent(project -> {
            scoreboardService.updateScoreboard(project, projectService.getStorage());
            callBuilders(project);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnProjectFinalize(FinalizeProjectEvent event) {
        projectService.finalizeProject(event.getProject());
        updateProjectScoreboard();
    }

    private void addPlannedBlocks(BuildProject project, List<StructBlockModel> brokenBlocks) {
        for (StructBlockModel block : brokenBlocks) {
            project.addPlannedBlock(block);
        }
    }

    private void callBuilders(BuildProject project) {
        List<StructNpc> builders = npcService.getNpcByRole(NpcRole.BUILDER);
        int possibleTasksNumber = Math.min(project.getPlanSize(), projectService.getStorage().getResources());
        if (builders.size() > 0 && possibleTasksNumber > 0) {
            int builderIndex = 0;
            for (int i = 0; i < possibleTasksNumber; i++) {
                if (builderIndex == builders.size()) {
                    builderIndex = 0;
                }
                addTask(builders.get(builderIndex), project);
                builderIndex++;
            }
        }
    }

    private void addTask(StructNpc npc, BuildProject project) {
        StructBlockModel block = project.getPlannedBlock();
        projectService.getStorage().updateResources(-1);
        BuildTask task = new BuildTaskImpl(
                npcService.getPlugin(),
                (Villager) Bukkit.getEntity(npc.getEntityId()),
                block,
                Bukkit.getWorld(npc.getWorld())
                        .getBlockAt(block.getX(), block.getY(), block.getZ()).getLocation());
        taskService.add(task);
    }

    void updateProjectScoreboard() {
        if (projectService.getCurrent().isPresent()) {
            scoreboardService.updateScoreboard(projectService.getCurrent().get(), projectService.getStorage());
        } else {
            scoreboardService.closeScoreboard(BoardType.PROJECT);
        }
    };
}
