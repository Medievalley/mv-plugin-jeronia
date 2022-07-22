package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.domain.models.IStructure;

public interface StructDamagedCallback {

    void calculateDamage(long destroyedPercent, IStructure struct);
}
