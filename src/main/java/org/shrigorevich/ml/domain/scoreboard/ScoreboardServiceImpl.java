package org.shrigorevich.ml.domain.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.shrigorevich.ml.domain.BaseService;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.Storage;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardServiceImpl extends BaseService implements ScoreboardService {

    private final Map<BoardType, Scoreboard> boards;

    public ScoreboardServiceImpl(Plugin plugin) {
        super(plugin);
        boards = new HashMap<>();
    }

    @Override
    public Scoreboard getScoreboard(BoardType type) {
        if (!boards.containsKey(type)) {
            boards.put(type, Bukkit.getScoreboardManager().getNewScoreboard());
        }
        return boards.get(type);
    }

    @Override
    public Objective createObjective(BoardType type, DisplaySlot slot, String displayName) {
        Objective objective = getScoreboard(type).registerNewObjective(
                type.toString(),
                "dummy",
                Component.text(ChatColor.RED + displayName),
                RenderType.INTEGER);
        objective.setDisplaySlot(slot);
        return objective;
    }

    @Override
    public void updateScoreboard(BuildProject project, Storage storage) {
        Scoreboard board = getScoreboard(BoardType.PROJECT);
        Objective curObjective = board.getObjective(BoardType.PROJECT.toString());

        if (curObjective != null) {
            curObjective.unregister();
        }
        Objective objective = createObjective(
                BoardType.PROJECT, DisplaySlot.SIDEBAR,
                "Project: " + project.getStruct().getName()
        );

        int resourceNeeded = project.getPlanSize() - storage.getResources();
        objective.getScore("Resources needed:").setScore(Math.max(resourceNeeded, 0));
        objective.getScore("Health:").setScore(project.getHealthPercent());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(board);
        }
    }

    @Override
    public void closeScoreboard(BoardType type) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }
}
