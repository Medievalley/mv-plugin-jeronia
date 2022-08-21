package org.shrigorevich.ml.domain.ai;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTask implements Task {
    private final TaskPriority priority;
    private final Plugin plugin;
    private final Mob mob;
    private final TaskData data;
    private boolean blocked;

    public BaseTask(Plugin plugin, TaskType type, TaskPriority priority, Mob mob) {
        this.priority = priority;
        this.plugin = plugin;
        this.mob = mob;
        this.data = new TaskDataImpl(type);
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
    public TaskData getData() {
        return data;
    }

    @Override
    public int compareTo(@NotNull Task task) {
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
        return data.getType();
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


}
