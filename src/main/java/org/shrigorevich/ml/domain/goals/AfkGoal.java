package org.shrigorevich.ml.domain.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;

public class AfkGoal implements Goal<Villager> {
    private final GoalKey<Villager> key;
    private final Mob mob;
    private boolean activated = false;
    private final Location spawn;

    public AfkGoal(Plugin plugin, Mob mob, Location spawn) {
        this.key = GoalKey.of(Villager.class, new NamespacedKey(plugin, "afk"));
        this.mob = mob;
        this.spawn = spawn;
    }

    @Override
    public boolean shouldActivate() {
        if(!activated) {
            activated = true;
            Bukkit.broadcast(Component.text(ChatColor.BLUE + "Goal activated"));
        }
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }

    @Override
    public void start() {
        mob.getPathfinder().moveTo(spawn, 1.0D);
    }

    @Override
    public void stop() {
        Bukkit.broadcast(Component.text(ChatColor.BLUE + "Goal stopped"));
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        mob.getPathfinder().moveTo(spawn, 1.0D);
    }

    @Override
    public GoalKey<Villager> getKey() {
        return key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }
}