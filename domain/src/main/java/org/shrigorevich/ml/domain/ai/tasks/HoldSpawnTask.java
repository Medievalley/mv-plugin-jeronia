package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.*;
import org.shrigorevich.ml.domain.ai.goals.legacy.ReachLocationGoal;

public class HoldSpawnTask extends BasePriorityTask implements PriorityTask {
    private final Location target;
    private Goal<Mob> goal;
    public HoldSpawnTask(Plugin plugin, Mob entity, Location l) {
        super(plugin, TaskType.HOLD_SPAWN, TaskPriority.LOW, entity);
        this.target = l;
    }
    @Override
    public void start() {
        MobGoals goals = getPlugin().getServer().getMobGoals();
        goal = new ReachLocationGoal(getPlugin(), this, getEntity(), target);
        setGoal(goals, goal);
    }

    @Override
    public void end() {
        if (goal != null) {
            getPlugin().getServer().getMobGoals().removeGoal(
                    getEntity(),
                    goal.getKey()
            );
        }
    }

    @Override
    public boolean shouldBeBlocked() {
        Pathfinder.PathResult rp = getEntity().getPathfinder().findPath(target);
        return rp.getFinalPoint() == null || distanceSquared(rp.getFinalPoint(), target);
    }
}