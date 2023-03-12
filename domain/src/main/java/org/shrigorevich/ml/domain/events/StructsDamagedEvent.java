package org.shrigorevich.ml.domain.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structures.StructBlock;

import java.util.List;
import java.util.Map;

public class StructsDamagedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Map<Integer, List<StructBlock>> blocksPerStruct;

    public StructsDamagedEvent(Map<Integer, List<StructBlock>> blocksPerStruct) {

        this.blocksPerStruct = blocksPerStruct;
    }

    public Map<Integer, List<StructBlock>> getBrokenBlocks() {
        return blocksPerStruct;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
