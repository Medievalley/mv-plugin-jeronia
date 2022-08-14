package org.shrigorevich.ml.domain.ai;

import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.BaseService;

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

            if (cur != null && top != null) {
                System.out.printf("Cur priority: %d. Top priority: %d%n",
                        cur.getPriority().getValue(), top.getPriority().getValue());

                return cur.getPriority().getValue() < top.getPriority().getValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void stopCurrent(UUID entityId) {
        NpcTask task = currentTasks.get(entityId);
        if (task != null) {
            task.end();
        }
    }
}
