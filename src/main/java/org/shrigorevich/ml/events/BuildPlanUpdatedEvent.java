package org.shrigorevich.ml.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;

public class BuildPlanUpdatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final LoreStructure structure;
    private final List<StructBlockModel> brokenBlocks;

    public BuildPlanUpdatedEvent(LoreStructure structure, List<StructBlockModel> brokenBlocks) {

        this.structure = structure;
        this.brokenBlocks = brokenBlocks;
    }

    public List<StructBlockModel> getBrokenBlocks() {
        return brokenBlocks;
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
