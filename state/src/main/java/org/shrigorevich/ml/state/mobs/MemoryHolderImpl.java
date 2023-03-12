package org.shrigorevich.ml.state.mobs;

import org.shrigorevich.ml.domain.mobs.MemoryHolder;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.MemoryUnit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MemoryHolderImpl implements MemoryHolder {

    private final EnumMap<MemoryKey, List<MemoryUnit>> memory;

    public MemoryHolderImpl() {
        this.memory = new EnumMap<>(MemoryKey.class);
        for (MemoryKey key : MemoryKey.values()) {
            memory.put(key, new ArrayList<>());
        }
    }

    @Override
    public List<MemoryUnit> getMemory(MemoryKey key) {
        return memory.get(key);
    }

    @Override
    public void addMemory(MemoryUnit unit) {
        memory.get(unit.getKey()).add(unit);
    }
}
