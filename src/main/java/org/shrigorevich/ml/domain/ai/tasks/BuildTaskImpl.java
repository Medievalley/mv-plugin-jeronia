package org.shrigorevich.ml.domain.ai.tasks;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
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
        goal = new BuildGoal(getPlugin(), this, getEntity(), target);
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
        return false;
    }
}