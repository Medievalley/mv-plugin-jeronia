package org.shrigorevich.ml.domain.goals;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.Service;

public interface GoalService extends Service {

    void setGoGoal(Villager npc, Location targetLocation);
}
