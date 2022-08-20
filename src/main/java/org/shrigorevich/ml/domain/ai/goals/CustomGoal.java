package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.ai.TaskData;
import org.shrigorevich.ml.events.LocationReachedEvent;

import java.util.EnumSet;

public class CustomGoal extends BaseGoal implements Goal<Mob> {
    private final Player player;
    private int cooldown = 0;

    public CustomGoal(Plugin plugin, TaskData data, Mob mob, Player player) {

        super(mob, plugin, data, ActionKey.REACH_LOCATION);
        this.player = player;
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
    }

    @Override
    public void stop() {
        getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        cooldown+=1;
        getMob().getPathfinder().moveTo(player, 0.7D);

        if (cooldown == 10) {
            cooldown = 0;
            Location loc = getMob().getPathfinder().getCurrentPath().getFinalPoint();
            if (loc != null) {
                player.sendMessage(String.format("Final: %d %d %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            } else {
                player.sendMessage(String.format("Final: No point"));
            }
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