package org.shrigorevich.ml.domain.mob.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RunRegularSpawnTimerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public RunRegularSpawnTimerEvent() {}

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
