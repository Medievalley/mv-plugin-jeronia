package org.shrigorevich.ml.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.contracts.BuildTask;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.domain.ai.tasks.BuildTaskImpl;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.contracts.StructNpc;
import org.shrigorevich.ml.domain.project.contracts.BuildProject;
import org.shrigorevich.ml.domain.project.BuildProjectImpl;
import org.shrigorevich.ml.domain.project.contracts.ProjectService;
import org.shrigorevich.ml.domain.scoreboard.BoardType;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.contracts.TownInfra;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BuildProjectHandler implements Listener {
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;
    private final TaskService taskService;
    private final StructureService structureService;
    private final Logger logger;

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
        this.logger = LogManager.getLogger("BuildProjectHandler");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStructsDamaged(StructsDamagedEvent event) {
        Map<Integer, List<StructBlockModel>> brokenBlocks = event.getBrokenBlocks();
        for (int structId : brokenBlocks.keySet()) {
            structureService.getStruct(structId).ifPresent(struct -> {
                if (struct instanceof TownInfra ti) {
                    try {
                        Optional<BuildProject> project = projectService.getProject(structId);
                        if (project.isEmpty()) {
                            int structBlocks = ti.getStructBlocks().size();
                            BuildProject newProject = new BuildProjectImpl(ti, structBlocks);
                            projectService.addProject(newProject);
                            project = Optional.of(newProject);
                        }
                        addPlannedBlocks(project.get(), brokenBlocks.get(structId));
                    } catch (Exception ex) {
                        //TODO: Escalate struct damaged error
                    }
                }
            });
        }

        projectService.getCurrent().ifPresent(project -> {
            if (brokenBlocks.containsKey(project.getId())) {
                callBuilders(project);
                scoreboardService.updateScoreboard(project, projectService.getResources());
                logger.info(String.format("Current project: %s. Storage: %d%n",
                    project.getStruct().getName(), projectService.getResources()));
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBuildStart(BuildStartedEvent event) {
        Entity entity = event.getEntity();
        StructBlockModel block = event.getTask().getBlock();

        projectService.getProject(block.getStructId()).ifPresent(p -> {
            try {
                structureService.restoreBlock(block);
                p.restoreOne();
                taskService.finalize(entity.getUniqueId());
                projectService.updateResources(-1);
                if (p.getBrokenSize() == 0) {
                    projectService.finalizeProject(p.getId());
                }
            } catch (Exception ex) {
                //TODO: inject logger
            }
        });
        updateProjectScoreboard();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnResourceSupplied(StorageReplenishedEvent event) {
        projectService.getCurrent().ifPresent(project -> {
            scoreboardService.updateScoreboard(project, projectService.getResources());
            callBuilders(project);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnProjectFinalize(FinalizeProjectEvent event) {
        projectService.finalizeProject(event.getProject().getId());
        updateProjectScoreboard();
    }

    private void addPlannedBlocks(BuildProject project, List<StructBlockModel> brokenBlocks) {
        for (StructBlockModel block : brokenBlocks) {
            project.addPlannedBlock(block);
        }
    }

    private void callBuilders(BuildProject project) {
        List<StructNpc> builders = npcService.getNpcByRole(NpcRole.BUILDER);
        int possibleTasksNumber = Math.min(project.getPlanSize(), projectService.getResources() - project.getBrokenSize() + project.getPlanSize());
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
        World world = Bukkit.getWorld(npc.getWorld());
        if (world != null) {
            BuildTask task = new BuildTaskImpl(
                    npcService.getPlugin(),
                    (Villager) Bukkit.getEntity(npc.getEntityId()),
                    block,
                    world.getBlockAt(block.getX(), block.getY(), block.getZ()).getLocation());
            taskService.add(task);
        } else {
            logger.error(String.format("Can not get the world by name: %s", npc.getWorld()));
        }

    }

    void updateProjectScoreboard() {
        if (projectService.getCurrent().isPresent()) {
            scoreboardService.updateScoreboard(projectService.getCurrent().get(), projectService.getResources());
        } else {
            scoreboardService.closeScoreboard(BoardType.PROJECT);
        }
    }
}
