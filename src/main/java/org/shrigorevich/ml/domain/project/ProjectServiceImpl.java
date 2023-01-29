package org.shrigorevich.ml.domain.project;

import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.AdventurePlugin;
import org.shrigorevich.ml.common.BaseService;

import java.util.*;

public class ProjectServiceImpl extends BaseService implements ProjectService {
    private final Map<Integer, BuildProject> projects;
    private final ProjectContext context;
    private final PriorityQueue<BuildProject> buildPlan;
    private Storage storage;

    public ProjectServiceImpl(Plugin plugin, ProjectContext context) {
        super(plugin, LogManager.getLogger("ProjectServiceImpl"));
        this.context = context;
        this.projects = new HashMap<>();
        this.buildPlan = new PriorityQueue<>();
    }

    @Override
    public Optional<BuildProject> getProject(int structId) {
        BuildProject project = projects.get(structId);
        return project == null ? Optional.empty() : Optional.of(project);
    }

    @Override
    public Optional<BuildProject> getCurrent() {
        BuildProject project = buildPlan.peek();
        return project == null ? Optional.empty() : Optional.of(project);
    }

    @Override
    public void addProject(BuildProject project) {
        projects.put(project.getId(), project);
        buildPlan.add(project);
    }

    @Override
    public void finalizeProject(int structId) {
        if (projects.containsKey(structId)) {
            BuildProject proj = projects.remove(structId);
            buildPlan.removeIf(p -> p.getId() == structId);
            //TODO: should be moved to higher logic lvl
            if (getPlugin() instanceof AdventurePlugin) {
                ((AdventurePlugin) getPlugin()).showTitle(
                    String.format("Project %s finalized", proj.getStruct().getName()),
                    "Congratulations", Color.AQUA.asRGB()
                );
            } else {
                Bukkit.broadcast(Component.text(String.format("Project %s finalized", proj.getStruct().getName())));
            }
        }
    }

    @Override
    public int getResources() {
        return storage.getResources();
    }

    @Override
    public int getDeposit() {
        return storage.getDeposit();
    }

    @Override
    public void updateResources(int amount) {
        try {
            storage.updateResources(amount);
            context.updateResources(storage.getId(), storage.getResources());
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void updateDeposit(int amount) {
        try {
            storage.updateDeposit(amount);
            context.updateDeposit(storage.getId(), storage.getDeposit());
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void setup() throws Exception {
        try {
            context.getStorage().ifPresentOrElse(model -> {
                this.storage = new StorageImpl(model);
            }, () -> getLogger().warn("Storage not configured"));
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while setup project service");
        }
    }
}
