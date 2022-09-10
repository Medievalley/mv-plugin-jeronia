package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Utils;

import java.util.EnumSet;

public class HoldGoal extends BaseGoal implements Goal<Mob> {
    private int cooldown = 0;

    public HoldGoal(Plugin plugin, Mob mob, Location target) {
        super(mob, target, plugin, ActionKey.REACH_LOCATION);
    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }

    @Override
    public void start() {
        move(0.7D);
    }

    @Override
    public void stop() {
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        cooldown+=1;
        updateStuckTicks();
        defineCurrentLocation();
        if (cooldown == 5) {
            cooldown = 0;
            move(0.7D);

            if (!isAchieved() && Utils.distanceSquared(getMobLocation(), getTarget())) {
                setAchieved(true);
                System.out.println("Achieved");
            }

        }

        if (getStuckTicks() == 20) {
            resetStuckTicks();
            processStuck();
        }
    }

    @Override
    public GoalKey<Mob> getKey() {
        return getGoalKey();
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }




}