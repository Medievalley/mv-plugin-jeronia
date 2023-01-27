package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.FoodStructure;

import java.util.List;

public class StructsLoadedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<FoodStructure> structures;

    public StructsLoadedEvent(List<FoodStructure> structures) {
        this.structures = structures;
    }

    public List<FoodStructure> getStructures() {
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
