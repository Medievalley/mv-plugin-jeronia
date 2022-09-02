package org.shrigorevich.ml.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.BuildEvent;

public class BuildHandler implements Listener {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final StructureService structureService;

    public BuildHandler(TaskService taskService, ProjectService projectService, StructureService structureService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBuildStart(BuildEvent event) {
        Entity entity = event.getEntity();
        StructBlockModel b = event.getTask().getBlock();
        entity.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.valueOf(b.getType()));
        projectService.getProject(b.getStructId()).ifPresent(p -> {
            p.decrementBrokenSize();
            structureService.getById(b.getStructId()).ifPresent(s -> {
                s.restoreBlock(b);
                taskService.finalize(event.getEntity().getUniqueId());
            });
        });
    }
}
