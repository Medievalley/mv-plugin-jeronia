package org.shrigorevich.ml.domain.ai;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.apache.logging.log4j.LogManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.ai.goals.DefaultGoal;

import java.util.*;

public class TaskServiceImpl extends BaseService implements TaskService {

    private final Map<UUID, PriorityQueue<PriorityTask>> tasksQueues;
    private final Map<UUID, PriorityTask> currentTasks;
    private final Map<UUID, List<PriorityTask>> blockedTasks;

    public TaskServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger("TaskServiceImpl"));
        tasksQueues = new HashMap<>();
        currentTasks = new HashMap<>();
        blockedTasks = new HashMap<>();
    }

    @Override
    public void add(PriorityTask task) {
        UUID entityId = task.getEntity().getUniqueId();
        if (tasksQueues.containsKey(entityId)) {
            PriorityQueue<PriorityTask> queue = tasksQueues.get(entityId);
            queue.add(task);
        } else {
            PriorityQueue<PriorityTask> queue = new PriorityQueue<>();
            queue.add(task);
            tasksQueues.put(entityId, queue);
        }
    }

    @Override
    public Optional<PriorityTask> get(UUID entityId) {
        PriorityTask task = currentTasks.get(entityId);
        return task == null ? Optional.empty() : Optional.of(task);
    }

    @Override
    public void finalize(UUID entityId) {
        PriorityTask task = currentTasks.remove(entityId);
        if (task != null) {
            task.end();
        }
    }

    @Override
    public void block(UUID entityId) {
        PriorityTask task = currentTasks.remove(entityId);
        if (task != null) {
            task.end();
            task.setBlocked(true);
            if (blockedTasks.containsKey(entityId)) {
                blockedTasks.get(entityId).add(task);
            } else {
                List<PriorityTask> list = new ArrayList<>();
                list.add(task);
                blockedTasks.put(entityId, list);
            }
        }
    }

    @Override
    public void clear(UUID entityId) {
        tasksQueues.remove(entityId);
        currentTasks.remove(entityId);
    }

    @Override
    public void startTopPriority(UUID entityId) {
        postpone(entityId);
        PriorityQueue<PriorityTask> queue = tasksQueues.get(entityId);
        if (queue != null && !queue.isEmpty()) {
            PriorityTask task = queue.poll();
            currentTasks.put(entityId, task);
            task.start();
        }
    }

    @Override
    public boolean shouldChangeTask(UUID entityId) {
        PriorityQueue<PriorityTask> queue = tasksQueues.get(entityId);
        if (queue != null) {
            PriorityTask cur = currentTasks.get(entityId);
            PriorityTask top = queue.peek();
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
            List<PriorityTask> updatedList = new ArrayList<>();
            for (PriorityTask task : blockedTasks.get(entityId)) {
                if (!task.shouldBeBlocked()) {
                    task.setBlocked(false);
                    tasksQueues.get(entityId).add(task);
                } else {
                    updatedList.add(task);
                }
            }
            blockedTasks.put(entityId, updatedList);
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
    public List<PriorityTask> getEntityTasks(UUID entityId) {
        if (tasksQueues.containsKey(entityId)) {
            return tasksQueues.get(entityId).stream().toList();
        }
        return new ArrayList<>();
    }

    private void postpone(UUID entityId) {
        PriorityTask task = currentTasks.remove(entityId);
        if (task != null) {
            task.end();
            tasksQueues.get(entityId).add(task);
        }
    }
}
