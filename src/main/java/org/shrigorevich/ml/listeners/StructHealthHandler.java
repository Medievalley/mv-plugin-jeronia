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

    public StructHealthHandler(StructureService structService) {
        this.structService = structService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnStructLoaded(ProjectUpdatedEvent event) {
        LoreStructure struct = event.getStructure();
        List<StructBlockDB> blocks = struct.getStructBlocks();
        Map<Material, Integer> materials = new HashMap<>();

        List<StructBlockDB> brokenBlocks = blocks.stream().filter(StructBlockDB::isBroken).collect(Collectors.toList());
        for (StructBlockDB b : brokenBlocks) {
            Material material = Material.valueOf(b.getType());
            if (materials.containsKey(material)) {
                materials.compute(material, (m, v) -> v+1);
            } else {
                materials.put(material, 1);
            }
        }

        System.out.printf("Current project: %d. Broken blocks: %d%n", struct.getId(), brokenBlocks.size());
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(
                "Village",
                "dummy",
                Component.text(ChatColor.BLUE+"Village"),
                RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (Material m : materials.keySet()) {
            Score score = objective.getScore(m.toString());
            score.setScore(materials.get(m));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(board);
        }
    }
}
