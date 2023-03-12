package org.shrigorevich.ml.domain.ai;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class BasePriorityTask implements PriorityTask {
    private final TaskPriority priority;
    private final Plugin plugin;
    private final Mob mob;
    private final TaskType type;
    private boolean blocked;

    public BasePriorityTask(Plugin plugin, TaskType type, TaskPriority priority, Mob mob) {
        this.priority = priority;
        this.plugin = plugin;
        this.mob = mob;
        this.type = type;
    }

    @Override
    public Mob getEntity() {
        return mob;
    }

    @Override
    public TaskPriority getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull PriorityTask task) {
        return task.getPriority().getValue() - this.priority.getValue();
    }

    @Override
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    @Override
    public boolean isBlocked() {
        return this.blocked;
    }

    @Override
    public TaskType getType() {
        return type;
    }

    public Plugin getPlugin() {
        return plugin;
    }
    public void setGoal(MobGoals goals, Goal<Mob> goal) {
        if (goals.hasGoal(mob, goal.getKey())) {
            goals.removeGoal(mob, goal.getKey());
        }
        goals.addGoal(mob, getPriority().getValue(), goal);
    }

    public boolean distanceSquared(Location l1, Location l2) {
        return l1.distanceSquared(l2) < 1;
    }
}
