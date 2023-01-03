package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.BaseTask;
import org.shrigorevich.ml.domain.ai.contracts.Task;
import org.shrigorevich.ml.domain.ai.TaskPriority;
import org.shrigorevich.ml.domain.ai.TaskType;
import org.shrigorevich.ml.domain.ai.goals.ReachLocationGoal;

public class GoSafeTask extends BaseTask implements Task {
    private final Location target;
    private Goal<Mob> goal;
    public GoSafeTask(Plugin plugin, Mob entity, Location l) {
        super(plugin, TaskType.GO_SAFE, TaskPriority.HIGH, entity);
        this.target = l;
//        System.out.printf("Task added: %s. At: %d %d %d%n", type, l.getBlockX(), l.getBlockY(), l.getBlockZ()); //TODO: comment
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
        return rp.getFinalPoint() == null || !Utils.distanceSquared(rp.getFinalPoint(), target);
    }
}