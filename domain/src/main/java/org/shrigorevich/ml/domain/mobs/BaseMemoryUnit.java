package org.shrigorevich.ml.domain.mobs;

public abstract class BaseMemoryUnit implements MemoryUnit {

    private final MemoryKey key;
    public BaseMemoryUnit(MemoryKey key) {
        this.key = key;
    }

    @Override
    public MemoryKey getKey() {
        return key;
    }
}
