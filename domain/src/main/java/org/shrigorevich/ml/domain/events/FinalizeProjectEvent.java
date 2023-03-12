package org.shrigorevich.ml.domain.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.project.BuildProject;

public class FinalizeProjectEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final BuildProject project;

    public FinalizeProjectEvent(BuildProject project) {
        this.project = project;
    }

    public BuildProject getProject() {
        return project;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
