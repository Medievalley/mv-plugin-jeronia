package org.shrigorevich.ml.domain.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.shrigorevich.ml.domain.BaseService;

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
    public Objective createObjective(BoardType type, DisplaySlot slot) {
        Objective objective = getScoreboard(type).registerNewObjective(
                type.toString(),
                "dummy",
                Component.text(ChatColor.BLUE + type.getName()),
                RenderType.INTEGER);
        objective.setDisplaySlot(slot);
        return objective;
    }
}
