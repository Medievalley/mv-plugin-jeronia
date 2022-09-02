package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
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
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.BuildPlanUpdatedEvent;
import org.shrigorevich.ml.events.ProjectRestoreEvent;

import java.util.List;
import java.util.stream.Collectors;

public class StructHealthHandler implements Listener {
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;
    private final TaskService taskService;

    public StructHealthHandler(ProjectService projectService, ScoreboardService scoreboardService, NpcService npcService, TaskService taskService) {
        this.projectService = projectService;
        this.scoreboardService = scoreboardService;
        this.npcService = npcService;
        this.taskService = taskService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnProjectRestore(ProjectRestoreEvent event) {
        LoreStructure struct = event.getStructure();
        List<StructBlockModel> blocks = struct.getStructBlocks();
        List<StructBlockModel> brokenBlocks = blocks.stream().filter(StructBlockModel::isBroken).collect(Collectors.toList());

        BuildProject project = new BuildProjectImpl(struct.getId(), blocks.size());
        updateProject(project, brokenBlocks);
        projectService.addProject(project);

        System.out.printf("Current project: %s. Broken blocks: %d%n", struct.getName(), project.getBrokenSize());
        updateScoreboard(struct, project);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlanUpdated(BuildPlanUpdatedEvent event) {
        LoreStructure struct = event.getStructure();
        BuildProject project = projectService.getProject(struct.getId()).orElseGet(() -> {
            List<StructBlockModel> blocks = struct.getStructBlocks();
            return new BuildProjectImpl(struct.getId(), blocks.size());
        });
        updateProject(project, event.getBrokenBlocks());
        projectService.addProject(project);
        System.out.printf("Current project: %s. Broken blocks: %d%n", struct.getName(), project.getBrokenSize());
        updateScoreboard(struct, project);

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

    private void updateProject(BuildProject project, List<StructBlockModel> brokenBlocks) {
        for (StructBlockModel block : brokenBlocks) {
            project.addPlannedBlock(block);
        }
    }

    private void updateScoreboard(LoreStructure struct, BuildProject project) {
        Scoreboard board = scoreboardService.getScoreboard(BoardType.PROJECT);
        Objective curObjective = board.getObjective(BoardType.PROJECT.toString());

        if (curObjective != null) {
            curObjective.unregister();
        }
        Objective objective = scoreboardService.createObjective(BoardType.PROJECT, DisplaySlot.SIDEBAR, "Project: " + struct.getName());

        objective.getScore("Resources needed:").setScore(project.getBrokenSize());
        objective.getScore("Health:").setScore(project.getHealthPercent());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(board);
        }
    }
}
