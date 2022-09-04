package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.LoreStructure;

import java.util.List;

public class StructsLoadedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<LoreStructure> structures;

    public StructsLoadedEvent(List<LoreStructure> structures) {
        this.structures = structures;
    }

    public List<LoreStructure> getStructures() {
        return structures;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
