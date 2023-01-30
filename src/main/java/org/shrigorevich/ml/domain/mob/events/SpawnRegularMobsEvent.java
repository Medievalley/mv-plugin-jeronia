package org.shrigorevich.ml.domain.mob.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpawnRegularMobsEvent extends Event {

    private boolean powerDefined;
    private int power;
    private static final HandlerList HANDLERS = new HandlerList();

    public SpawnRegularMobsEvent() {}

    public SpawnRegularMobsEvent(int power) {
        this.power = power;
        this.powerDefined = true;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isPowerDefined() {
        return powerDefined;
    }

    public int getPower() {
        return power;
    }
}
