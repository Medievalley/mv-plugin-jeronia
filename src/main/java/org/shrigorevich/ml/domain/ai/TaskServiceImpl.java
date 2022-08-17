package org.shrigorevich.ml.domain.ai;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.BaseService;
import org.shrigorevich.ml.domain.ai.goals.DefaultGoal;

import java.util.*;

public class TaskServiceImpl extends BaseService implements TaskService {

    private final Map<UUID, PriorityQueue<NpcTask>> tasksQueues;
    private final Map<UUID, NpcTask> currentTasks;

    public TaskServiceImpl(Plugin plugin) {
        super(plugin);
        tasksQueues = new HashMap<>();
        currentTasks = new HashMap<>();
    }

    @Override
    public void add(NpcTask task) {
        UUID entityId = task.getEntity().getUniqueId();

        if (tasksQueues.containsKey(entityId)) {
            PriorityQueue<NpcTask> queue = tasksQueues.get(entityId);
            queue.add(task);
            System.out.println("Task added: " + task.getData().getType());
        } else {
            PriorityQueue<NpcTask> queue = new PriorityQueue<>();
            queue.add(task);
            tasksQueues.put(entityId, queue);
            System.out.println("Queue with task created");
        }
    }

    @Override
    public void finalizeCurrent(UUID entityId) {
        NpcTask task = currentTasks.get(entityId);
        if (task != null) {
            task.end();
            tasksQueues.get(entityId).remove(task);
            currentTasks.remove(entityId);
        }
    }

    @Override
    public void startTopPriority(UUID entityId) {
        stopCurrent(entityId);
        PriorityQueue<NpcTask> queue = tasksQueues.get(entityId);
        if (queue != null && queue.peek() != null) {
            NpcTask task = queue.peek();
            currentTasks.put(entityId, task);
            task.start();
        }
    }

    @Override
    public boolean shouldChangeTask(UUID entityId) {
        PriorityQueue<NpcTask> queue = tasksQueues.get(entityId);
        if (queue != null) {
            NpcTask cur = currentTasks.get(entityId);
            NpcTask top = queue.peek();
            if (cur == null && top != null) {
                return true;

            } else if (cur != null && top != null) {
                return cur.getPriority().getValue() < top.getPriority().getValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void setDefaultAI(Entity entity) {
        MobGoals goals = getPlugin().getServer().getMobGoals();

        if (entity != null) {
            Villager npc = (Villager) entity;
            Goal<Villager> goal = new DefaultGoal(this, npc);

            if (goals.hasGoal(npc, goal.getKey())) {
                goals.removeGoal(npc, goal.getKey());
            }
            goals.addGoal(npc, 3, goal);
        }
    }

    private void stopCurrent(UUID entityId) {
        NpcTask task = currentTasks.get(entityId);
        if (task != null) {
            task.end();
        }
    }
}
