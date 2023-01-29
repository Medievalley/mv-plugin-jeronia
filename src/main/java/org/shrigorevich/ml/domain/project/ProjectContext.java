
package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.project.models.StorageModel;

import java.util.Optional;

public interface ProjectContext {
    Optional<StorageModel> getStorage() throws Exception;
    void updateResources(int structId, int amount) throws Exception;
    void updateDeposit(int structId, int amount) throws Exception;
}
