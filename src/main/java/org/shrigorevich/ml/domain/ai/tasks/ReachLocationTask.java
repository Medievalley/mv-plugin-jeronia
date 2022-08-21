package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.*;
import org.shrigorevich.ml.domain.ai.goals.HarvestGoal;
import org.shrigorevich.ml.domain.ai.goals.ReachLocationGoal;

public class ReachLocationTask extends BaseTask implements NpcTask {
    private final Location target;
    private Goal<Mob> goal;
    public ReachLocationTask(Plugin plugin, TaskType type, TaskPriority priority, Mob entity, Location l) {
        super(plugin, type, priority, entity);
        this.target = l;
        System.out.printf("Task added: %s. At: %d %d %d%n", type, l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
    @Override
    public void start() {
        MobGoals goals = getPlugin().getServer().getMobGoals();

        switch (getData().getType()) {
            case HARVEST:
                goal = new HarvestGoal(getPlugin(), getData(), getEntity(), target);
                break;
            case HOLD_SPAWN:
            case GO_SAFE:
            default:
                goal = new ReachLocationGoal(getPlugin(), getData(), getEntity(), target);
                break;
        }
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
//        Location fp = rp.getFinalPoint();
//        System.out.printf("Fp: %d %d %d. Target: %d %d %d%n",
//                fp.getBlockX(), fp.getBlockY(), fp.getBlockZ(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
        return rp.getFinalPoint() == null || !Utils.isLocationsEquals(rp.getFinalPoint(), target);
    }
}