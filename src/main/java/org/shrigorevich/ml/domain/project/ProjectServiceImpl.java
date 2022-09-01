package org.shrigorevich.ml.domain.project;

import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.contexts.ProjectContext;
import org.shrigorevich.ml.domain.BaseService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProjectServiceImpl extends BaseService implements ProjectService {
    private final Map<Integer, BuildProject> projects;
    private final Storage storage;
    private final ProjectContext context;
    public ProjectServiceImpl(Plugin plugin, ProjectContext context) {
        super(plugin);
        this.context = context;
        this.projects = new HashMap<>();
        this.storage = new StorageImpl();
    }

    @Override
    public Optional<BuildProject> getProject(int structId) {
        BuildProject project = projects.get(structId);
        return project == null ? Optional.empty() : Optional.of(project);
    }

    @Override
    public void addProject(BuildProject project) {
        projects.put(project.getStructId(), project);
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void load() {
        //TODO: load storage
    }
}
