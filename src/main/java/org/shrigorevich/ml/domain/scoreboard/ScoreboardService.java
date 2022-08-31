package org.shrigorevich.ml.domain.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.shrigorevich.ml.domain.Service;

public interface ScoreboardService extends Service {
    Scoreboard getScoreboard(BoardType type);
    Objective createObjective(BoardType type, DisplaySlot slot);
}
