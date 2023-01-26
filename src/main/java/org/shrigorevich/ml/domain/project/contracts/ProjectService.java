package org.shrigorevich.ml.domain.project.contracts;

import org.shrigorevich.ml.common.Service;

import java.util.Optional;

public interface ProjectService extends Service {

    Optional<BuildProject> getProject(int structId);
    Optional<BuildProject> getCurrent();
    void addProject(BuildProject project);
    void finalizeProject(int structId);
    int getResources();
    void updateResources(int amount);
    void load();
}
