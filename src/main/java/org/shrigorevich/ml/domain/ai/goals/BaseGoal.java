package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.GoalKey;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.TaskData;

public abstract class BaseGoal {

    private final GoalKey<Mob> key;
    private boolean achieved;
    private final TaskData data;
    private final Mob mob;
    private final Plugin plugin;

    public BaseGoal(Mob mob, Plugin plugin, TaskData data, ActionKey key) {

        this.key = GoalKey.of(Mob.class, new NamespacedKey(plugin, key.toString()));
        this.data = data;
        this.achieved = false;
        this.mob = mob;
        this.plugin = plugin;

        System.out.println(this.key);
    }

    protected boolean isAchieved() {
        return achieved;
    }

    protected void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    protected Mob getMob() {
        return mob;
    }

    protected TaskData getData() {
        return data;
    }

    protected Plugin getPlugin() {
        return plugin;
    }

    protected GoalKey<Mob> getGoalKey() {
        return key;
    }
}
