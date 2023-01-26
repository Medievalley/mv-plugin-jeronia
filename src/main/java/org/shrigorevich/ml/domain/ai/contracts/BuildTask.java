package org.shrigorevich.ml.domain.ai.contracts;

import org.shrigorevich.ml.domain.structure.contracts.StructBlock;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

public interface BuildTask extends Task {
    StructBlock getBlock();
}
