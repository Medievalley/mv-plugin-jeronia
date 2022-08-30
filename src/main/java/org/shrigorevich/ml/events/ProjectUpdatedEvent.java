package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.LoreStructure;

public class ProjectUpdatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final LoreStructure structure;

    public ProjectUpdatedEvent(LoreStructure structure) {

        this.structure = structure;
    }

    public LoreStructure getStructure() {
        return structure;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
