package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;

public class GoGoal implements Goal<Villager> {
    private final GoalKey<Villager> key;
    private final Mob mob;
    private boolean activated = false;
    private final Location target;

    public GoGoal(Plugin plugin, Mob mob, Location target) {
        this.key = GoalKey.of(Villager.class, new NamespacedKey(plugin, GoalKeys.GO_TO_LOCATION.toString()));
        this.mob = mob;
        this.target = target;
    }

    @Override
    public boolean shouldActivate() {
        if(!activated) {
            activated = true;
            System.out.println(String.format("GoGoal activated. Target location: %d %d %d", target.getBlockX(), target.getBlockY(), target.getBlockZ()));
        }
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }

    @Override
    public void start() {
        mob.getPathfinder().moveTo(target, 0.7D); //TODO: get from config
    }

    @Override
    public void stop() {
        System.out.println("GoGoal stopped");
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        mob.getPathfinder().moveTo(target, 0.7D);
    }

    @Override
    public GoalKey<Villager> getKey() {
        return key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}