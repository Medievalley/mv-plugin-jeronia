package org.shrigorevich.ml.domain.ai;


import org.shrigorevich.ml.domain.structures.StructBlock;

public interface BuildTask extends PriorityTask {
    StructBlock getBlock();
}
