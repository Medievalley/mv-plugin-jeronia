package org.shrigorevich.ml.domain.project;

import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.contexts.ProjectContext;
import org.shrigorevich.ml.domain.BaseService;
import org.shrigorevich.ml.domain.project.models.StorageModel;

import java.util.*;

public class ProjectServiceImpl extends BaseService implements ProjectService {
    private final Map<Integer, BuildProject> projects;
    private Storage storage;
    private final ProjectContext context;
    private final PriorityQueue<BuildProject> buildPlan;

    public ProjectServiceImpl(Plugin plugin, ProjectContext context) {
        super(plugin);
        this.context = context;
        this.projects = new HashMap<>();
        this.storage = new StorageImpl(100, 3);
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
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void load() {
        StorageModel storageModel = context.getStorage();
        this.storage = new StorageImpl(storageModel);
    }
}
