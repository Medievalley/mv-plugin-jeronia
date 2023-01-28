
package org.shrigorevich.ml.domain.project;

import org.shrigorevich.ml.domain.project.models.StorageModel;

public interface ProjectContext {
    StorageModel getStorage(int storageId);
    void updateResources(int structId, int amount);
}
