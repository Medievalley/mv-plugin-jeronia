package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ReplenishStorageEvent extends Event {
    private final int amount;
    private static final HandlerList HANDLERS = new HandlerList();

    public ReplenishStorageEvent(int amount) {
        this.amount = Math.abs(amount);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
