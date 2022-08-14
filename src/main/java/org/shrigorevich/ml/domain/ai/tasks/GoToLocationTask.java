package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.BaseTask;
import org.shrigorevich.ml.domain.ai.NpcTask;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.goals.GoGoal;

public class GoToLocationTask extends BaseTask implements NpcTask {
    private final Location target;
    private Goal<Villager> goal;

    public GoToLocationTask(Plugin plugin, TaskPriority priority, Entity entity, Location location) {
        super(plugin, priority, entity);
        this.target = location;
    }
    @Override
    public void start() {
        setInProgress(true);
        Villager npc = (Villager) getEntity();

        MobGoals goals = getPlugin().getServer().getMobGoals();
        goal = new GoGoal(getPlugin(), npc, target);

        if (goals.hasGoal(npc, goal.getKey())) {
            goals.removeGoal(npc, goal.getKey());
        }

        goals.addGoal(npc, getPriority().getValue(), goal);
    }

    @Override
    public void end() {
        setInProgress(false);
        if (goal != null) {
            getPlugin().getServer().getMobGoals().removeGoal(
                    (Villager) getEntity(),
                    goal.getKey()
            );
        }
    }
}
