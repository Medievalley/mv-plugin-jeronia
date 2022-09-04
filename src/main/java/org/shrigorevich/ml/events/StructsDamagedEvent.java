package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;
import java.util.Map;

public class StructsDamagedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Map<Integer, List<StructBlockModel>> blocksPerStruct;

    public StructsDamagedEvent(Map<Integer, List<StructBlockModel>> blocksPerStruct) {

        this.blocksPerStruct = blocksPerStruct;
    }

    public Map<Integer, List<StructBlockModel>> getBrokenBlocks() {
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
