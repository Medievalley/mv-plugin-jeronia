package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.*;
import org.shrigorevich.ml.domain.ai.goals.BuildGoal;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

public class BuildTaskImpl extends BaseTask implements BuildTask {
    private final Location target;
    private Goal<Mob> goal;
    private final StructBlockModel block;

    public BuildTaskImpl(Plugin plugin, Mob entity, StructBlockModel block, Location l) {
        super(plugin, TaskType.BUILD, TaskPriority.MIDDLE, entity);
        this.block = block;
        this.target = l;
    }

    @Override
    public StructBlockModel getBlock() {
        return block;
    }

    @Override
    public void start() {

        MobGoals goals = getPlugin().getServer().getMobGoals();
        Location finalPoint = defineFinalLocation(target);
//        System.out.printf("Init target: %d %d %d%n", target.getBlockX(), target.getBlockY(), target.getBlockZ());
//        System.out.printf("Final: %d %d %d%n", finalPoint.getBlockX(), finalPoint.getBlockY(), finalPoint.getBlockZ());

        goal = new BuildGoal(getPlugin(), this, getEntity(), finalPoint);
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

    private Location defineFinalLocation(Location target) {
        Pathfinder.PathResult path = getEntity().getPathfinder().findPath(target);
        if (path != null) {
            Location fp = path.getFinalPoint();
            if (Utils.distanceSquared(path.getFinalPoint(), target) && path.getPoints().size() > 1) {
                return path.getPoints().get(path.getPoints().size() - 2);
            } else {
                return fp;
            }
        } else {
            return target;
        }
    }

    @Override
    public boolean shouldBeBlocked() {
        return false;
    }
}