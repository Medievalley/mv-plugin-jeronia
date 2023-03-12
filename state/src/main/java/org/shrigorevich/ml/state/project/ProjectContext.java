
package org.shrigorevich.ml.state.project;

import org.shrigorevich.ml.state.project.models.StorageModel;

import java.util.Optional;

public interface ProjectContext {
    Optional<StorageModel> getStorage(int mainStructId) throws Exception;
    void updateResources(int structId, int amount) throws Exception;
    void updateDeposit(int structId, int amount) throws Exception;
}
