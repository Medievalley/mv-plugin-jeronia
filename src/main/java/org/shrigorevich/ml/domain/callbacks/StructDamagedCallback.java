package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.domain.structure.Structure;

public interface StructDamagedCallback {

    void calculateDamage(long destroyedPercent, Structure struct);
}
