package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.ai.contracts.TaskService;
import org.shrigorevich.ml.events.NpcInDangerEvent;

import java.util.EnumSet;
import java.util.List;

public class DefaultGoal implements Goal<Villager> {
    private final GoalKey<Villager> key;
    private final Mob mob;
    private final TaskService taskService;
    private int checkTasksTimer;
    private int checkBlockedTasksTimer;
    private int checkDangerTimer;

    public DefaultGoal(TaskService taskService, Mob mob) {
        this.taskService = taskService;
        this.key = GoalKey.of(Villager.class, new NamespacedKey(taskService.getPlugin(), ActionKey.DEFAULT_AI.toString()));
        this.mob = mob;
        this.checkTasksTimer = 0;
        this.checkBlockedTasksTimer = 0;
        this.checkDangerTimer = 0;
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
//        System.out.println("DefaultGoal activated");
    }

    @Override
    public void stop() {
//        System.out.println("DefaultGoal stopped");
    }

    @Override
    public void tick() {
        checkTasksTimer+=1;
        checkBlockedTasksTimer+=1;
        checkDangerTimer+=1;

        if (checkTasksTimer == 20) {
            checkTasksTimer = 0;
            //TODO: Move to event handler
            boolean shouldChangeTask = taskService.shouldChangeTask(mob.getUniqueId());
            if (shouldChangeTask) {
                Bukkit.getScheduler().runTask(taskService.getPlugin(), () -> {
                    taskService.startTopPriority(mob.getUniqueId());
                });
            }
        }
        if (checkBlockedTasksTimer == 100) {
            checkBlockedTasksTimer = 0;
            Bukkit.getScheduler().runTask(taskService.getPlugin(), () -> {
                taskService.checkBlockedTasks(mob.getUniqueId());
            });
        }
        if (checkDangerTimer == 10) {
            checkDangerTimer = 0;
            List<Entity> entities = mob.getNearbyEntities(7, 5, 7);
            if (entities.stream().anyMatch(e -> e instanceof Monster)) {
                Bukkit.getPluginManager().callEvent(new NpcInDangerEvent(mob));
            }
        }
    }

    @Override
    public GoalKey<Villager> getKey() {
        return key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.UNKNOWN_BEHAVIOR);
    }
}