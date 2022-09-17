package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.Service;

import java.util.Optional;

public interface ProjectService extends Service {

    Optional<BuildProject> getProject(int structId);
    Optional<BuildProject> getCurrent();
    void addProject(BuildProject project);
    void finalizeProject(BuildProject project);
    Storage getStorage();
    void load();
}
