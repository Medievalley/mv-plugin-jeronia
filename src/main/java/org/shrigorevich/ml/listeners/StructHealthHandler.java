package org.shrigorevich.ml.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;
import org.shrigorevich.ml.domain.scoreboard.BoardType;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockDB;
import org.shrigorevich.ml.events.ProjectUpdatedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StructHealthHandler implements Listener {

    private final StructureService structService;
    private final ScoreboardService scoreboardService;

    public StructHealthHandler(StructureService structService, ScoreboardService scoreboardService) {
        this.structService = structService;
        this.scoreboardService = scoreboardService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnProjectUpdated(ProjectUpdatedEvent event) {
        LoreStructure struct = event.getStructure();
        List<StructBlockDB> blocks = struct.getStructBlocks();

        List<StructBlockDB> brokenBlocks = blocks.stream().filter(StructBlockDB::isBroken).collect(Collectors.toList());

        System.out.printf("Current project: %d. Broken blocks: %d%n", struct.getId(), brokenBlocks.size());
        updateScoreboard(struct, blocks.size(), brokenBlocks.size());
    }

    private void updateScoreboard(LoreStructure struct, int blocks, int brokenBlocks) {
        Scoreboard board = scoreboardService.getScoreboard(BoardType.PROJECT);
        Objective curObjective = board.getObjective(BoardType.PROJECT.toString());

        if (curObjective != null) {
            curObjective.unregister();
        }
        Objective objective = scoreboardService.createObjective(BoardType.PROJECT, DisplaySlot.SIDEBAR, "Project: " + struct.getName());

        int health = 100 - (brokenBlocks > 0 ? brokenBlocks * 100 / blocks : 0);
        objective.getScore("Resources needed:").setScore(brokenBlocks);
        objective.getScore("Health:").setScore(health);
        for (Player p : Bukkit.getOnlinePlayers()) {
            Objective obj = p.getScoreboard().getObjective(BoardType.PROJECT.getName());
            if (obj != null) {
                p.setScoreboard(board);
            }
        }
    }
}
