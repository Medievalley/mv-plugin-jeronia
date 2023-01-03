
package org.shrigorevich.ml.domain.project.contracts;

import org.shrigorevich.ml.domain.project.models.StorageModel;

public interface ProjectContext {
    StorageModel getStorage();
    void updateResources(int amount);
}
