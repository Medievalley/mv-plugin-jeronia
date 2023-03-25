package org.shrigorevich.ml.domain.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.CustomMob;

public class ExploreEnvironmentEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final CustomMob customMob;

    public ExploreEnvironmentEvent(CustomMob entity) {
        this.customMob = entity;
    }

    public CustomMob getMob() {
        return customMob;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
