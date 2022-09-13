package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.BaseTask;
import org.shrigorevich.ml.domain.ai.Task;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.goals.HarvestGoal;

public class HarvestTask extends BaseTask implements Task {
    private final Location target;
    private Goal<Mob> goal;
    public HarvestTask(Plugin plugin, Mob entity, Location l) {
        super(plugin, TaskType.HARVEST, TaskPriority.MIDDLE, entity);
        this.target = l;
    }
    @Override
    public void start() {
        MobGoals goals = getPlugin().getServer().getMobGoals();
        goal = new HarvestGoal(getPlugin(), this, getEntity(), target);
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
        return rp.getFinalPoint() == null || !Utils.distanceSquared(rp.getFinalPoint(), target);
    }
}