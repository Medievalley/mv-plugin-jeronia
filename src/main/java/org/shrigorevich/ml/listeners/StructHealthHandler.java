package org.shrigorevich.ml.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.BuildProjectImpl;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.scoreboard.BoardType;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockDB;
import org.shrigorevich.ml.events.BuildPlanUpdatedEvent;
import org.shrigorevich.ml.events.ProjectRestoreEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StructHealthHandler implements Listener {
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;

    public StructHealthHandler(ProjectService projectService, ScoreboardService scoreboardService, NpcService npcService) {
        this.projectService = projectService;
        this.scoreboardService = scoreboardService;
        this.npcService = npcService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnProjectRestore(ProjectRestoreEvent event) {
        LoreStructure struct = event.getStructure();
        List<StructBlockDB> blocks = struct.getStructBlocks();
        List<StructBlockDB> brokenBlocks = blocks.stream().filter(StructBlockDB::isBroken).collect(Collectors.toList());

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
            List<StructBlockDB> blocks = struct.getStructBlocks();
            return new BuildProjectImpl(struct.getId(), blocks.size());
        });
        updateProject(project, event.getBrokenBlocks());
        projectService.addProject(project);
        System.out.printf("Current project: %s. Broken blocks: %d%n", struct.getName(), project.getBrokenSize());
        updateScoreboard(struct, project);
    }

    private void updateProject(BuildProject project, List<StructBlockDB> brokenBlocks) {
        for (StructBlockDB block : brokenBlocks) {
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
