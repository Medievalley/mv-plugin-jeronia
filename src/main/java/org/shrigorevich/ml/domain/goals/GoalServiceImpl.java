package org.shrigorevich.ml.domain.goals;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.BaseService;

public class GoalServiceImpl extends BaseService implements GoalService {


    public GoalServiceImpl(Plugin plugin) {
        super(plugin);
    }


    public void setGoGoal(Villager npc, Location targetLocation) {
        MobGoals goals = getPlugin().getServer().getMobGoals();
        GoGoal goal = new GoGoal(getPlugin(), npc, targetLocation);

        if (goals.hasGoal(npc, goal.getKey())) {
            goals.removeGoal(npc, goal.getKey());
        }

        goals.addGoal(npc, 1, goal);
    }
}
