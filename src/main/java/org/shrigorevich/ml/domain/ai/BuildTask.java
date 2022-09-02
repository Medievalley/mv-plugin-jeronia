package org.shrigorevich.ml.domain.ai;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

public interface BuildTask extends Task {
    StructBlockModel getBlock();
}
