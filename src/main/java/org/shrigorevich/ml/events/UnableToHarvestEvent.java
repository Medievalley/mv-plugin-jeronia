package org.shrigorevich.ml.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.ai.Task;

public class UnableToHarvestEvent extends Event implements Cancellable {

    private final Entity entity;
    private final Location target;
    private final Task task;
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    public UnableToHarvestEvent(Entity entity, Location target, Task task) {
        this.entity = entity;
        this.target = target;
        this.task = task;
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getTarget() {
        return target;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean result) {
        this.cancelled = result;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
