package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.*;
import org.shrigorevich.ml.domain.ai.goals.ReachLocationGoal;

public class ReachLocationTask extends BaseTask implements NpcTask, Locationable {
    private final Location target;
    private Goal<Mob> goal;
    public ReachLocationTask(Plugin plugin, TaskType type, TaskPriority priority, Entity entity, Location location) {
        super(plugin, type, priority, entity);
        this.target = location;
    }
    @Override
    public void start() {
        setInProgress(true);
        Villager npc = (Villager) getEntity();

        MobGoals goals = getPlugin().getServer().getMobGoals();
        goal = new ReachLocationGoal(getPlugin(), getData(), npc, target);

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

    @Override
    public Location getTargetLocation() {
        return target;
    }
}