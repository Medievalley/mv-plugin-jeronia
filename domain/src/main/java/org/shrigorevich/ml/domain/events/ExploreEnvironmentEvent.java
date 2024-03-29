package org.shrigorevich.ml.domain.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.ValleyMob;

public class ExploreEnvironmentEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ValleyMob valleyMob;

    public ExploreEnvironmentEvent(ValleyMob entity) {
        this.valleyMob = entity;
    }

    public ValleyMob getMob() {
        return valleyMob;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
