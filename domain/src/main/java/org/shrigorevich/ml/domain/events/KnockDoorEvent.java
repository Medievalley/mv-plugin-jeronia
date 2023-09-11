package org.shrigorevich.ml.domain.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.ValleyMob;

public class KnockDoorEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final ValleyMob mob;
    private final Location doorLocation;

    public KnockDoorEvent(ValleyMob mob, Location doorLocation) {
        this.mob = mob;
        this.doorLocation = doorLocation;
    }

    public ValleyMob getMob() {
        return mob;
    }
    public Location getDoorLocation() {
        return doorLocation;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
