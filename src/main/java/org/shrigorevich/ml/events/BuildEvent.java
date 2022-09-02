package org.shrigorevich.ml.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.ai.BuildTask;

public class BuildEvent extends Event {
    private final Entity entity;
    private final Location target;
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final BuildTask task;

    public BuildEvent(Entity entity, Location target, BuildTask task) {
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

    public BuildTask getTask() {
        return task;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
