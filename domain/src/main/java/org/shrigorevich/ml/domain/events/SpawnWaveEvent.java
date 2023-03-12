package org.shrigorevich.ml.domain.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnWaveEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public SpawnWaveEvent() {}

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
