package org.shrigorevich.ml.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FarmerJobStartEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Entity entity;
    private final Block block;

    public FarmerJobStartEvent(Entity entity, Block block) {
        this.entity = entity;
        this.block = block;
    }

    public Entity getEntity() {
        return entity;
    }

    public Block getBlock() {
        return block;
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