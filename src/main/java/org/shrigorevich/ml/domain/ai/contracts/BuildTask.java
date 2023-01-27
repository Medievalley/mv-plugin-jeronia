package org.shrigorevich.ml.domain.ai.contracts;

import org.shrigorevich.ml.domain.structure.StructBlock;

public interface BuildTask extends Task {
    StructBlock getBlock();
}
