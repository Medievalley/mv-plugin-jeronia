package org.shrigorevich.ml.domain.mobs;

import java.util.List;

public interface MemoryHolder {
    List<MemoryUnit> getMemory(MemoryKey key);

    void addMemory(MemoryUnit unit);

}
