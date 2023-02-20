package org.shrigorevich.ml.domain.ai;

import org.shrigorevich.ml.domain.structure.StructBlock;

public interface BuildTask extends PriorityTask {
    StructBlock getBlock();
}
