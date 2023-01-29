package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.common.Service;

import java.util.Optional;

public interface ProjectService extends Service {

    Optional<BuildProject> getProject(int structId);
    Optional<BuildProject> getCurrent();
    void addProject(BuildProject project);
    void finalizeProject(int structId);
    int getResources();
    int getDeposit();
    void updateResources(int amount);
    void updateDeposit(int amount);
    void setup() throws Exception;
}
