package org.shrigorevich.ml.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.ai.TaskData;

public class LocationReachedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Entity entity;
    private final Location location;
    private final TaskData taskData;

    public LocationReachedEvent(Entity entity, Location location, TaskData taskData) {
        this.entity = entity;
        this.location = location;
        this.taskData = taskData;
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public TaskData getTaskData() {
        return taskData;
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
