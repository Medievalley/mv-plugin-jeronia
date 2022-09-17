package org.shrigorevich.ml.domain.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.shrigorevich.ml.domain.Service;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.Storage;

public interface ScoreboardService extends Service {
    Scoreboard getScoreboard(BoardType type);
    Objective createObjective(BoardType type, DisplaySlot slot, String displayName);
    void updateScoreboard(BuildProject project, Storage storage);
    void closeScoreboard(BoardType type);
}
