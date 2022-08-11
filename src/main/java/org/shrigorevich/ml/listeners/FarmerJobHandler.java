package org.shrigorevich.ml.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.goals.GoalService;
import org.shrigorevich.ml.events.FarmerJobStartEvent;

public class FarmerJobHandler implements Listener {
    private final GoalService goalService;

    public FarmerJobHandler(GoalService goalService) {
        this.goalService = goalService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnJobStart(FarmerJobStartEvent event) {
        Block b = event.getBlock();
        Entity e = event.getEntity();
        goalService.setGoGoal((Villager) e, b.getLocation());
    }
}
