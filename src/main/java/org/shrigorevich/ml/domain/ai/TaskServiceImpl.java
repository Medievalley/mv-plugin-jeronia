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
    private final Map<UUID, List<NpcTask>> blockedTasks;

    public TaskServiceImpl(Plugin plugin) {
        super(plugin);
        tasksQueues = new HashMap<>();
        currentTasks = new HashMap<>();
        blockedTasks = new HashMap<>();
    }

    @Override
    public void add(NpcTask task) {
        UUID entityId = task.getEntity().getUniqueId();

        if (tasksQueues.containsKey(entityId)) {
            PriorityQueue<NpcTask> queue = tasksQueues.get(entityId);
            queue.add(task);
        } else {
            PriorityQueue<NpcTask> queue = new PriorityQueue<>();
            queue.add(task);
            tasksQueues.put(entityId, queue);
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

            } else if (cur != null && top != null ) {
                return cur.getPriority().getValue() < top.getPriority().getValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void checkBlockedTasks(UUID entityId) {
        if (blockedTasks.containsKey(entityId)) {
            List<NpcTask> updatedList = new ArrayList<>();
            for (NpcTask task : blockedTasks.get(entityId)) {
                if (!task.shouldBeBlocked()) {
                    task.setBlocked(false);
                    tasksQueues.get(entityId).add(task);
                } else {
                    updatedList.add(task);
                }
            }
            blockedTasks.put(entityId, updatedList);
            System.out.println("Blocked tasks size: " + blockedTasks.get(entityId).size());
        }
    }

    @Override
    public void blockCurrent(UUID entityId) {
        NpcTask task = currentTasks.remove(entityId);
        tasksQueues.get(entityId).remove(task);
        if (task != null) {
            task.end();
            task.setBlocked(true);
            if (blockedTasks.containsKey(entityId)) {
                blockedTasks.get(entityId).add(task);
            } else {
                List<NpcTask> list = new ArrayList<>();
                list.add(task);
                blockedTasks.put(entityId, list);
            }
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

    @Override
    public Optional<Task> getCurrent(UUID entityId) {
        Task task = currentTasks.get(entityId);
        return task == null ? Optional.empty() : Optional.of(task);
    }

    private void stopCurrent(UUID entityId) {
        NpcTask task = currentTasks.get(entityId);
        if (task != null) {
            task.end();
        }
    }
}
