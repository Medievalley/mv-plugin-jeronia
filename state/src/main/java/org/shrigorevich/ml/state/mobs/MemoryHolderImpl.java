package org.shrigorevich.ml.state.mobs;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.mobs.MemoryHolder;
import org.shrigorevich.ml.domain.mobs.MemoryKey;

import java.util.*;

public class MemoryHolderImpl implements MemoryHolder {

    private final EnumMap<MemoryKey, Queue<Location>> memory;

    public MemoryHolderImpl() {
        this.memory = new EnumMap<>(MemoryKey.class);
        for (MemoryKey key : MemoryKey.values()) {
            memory.put(key, new LinkedList<>());
        }
    }

    @Override
    public void addMemory(MemoryKey key, Location point) {
        memory.get(key).add(point);
    }

    @Override
    public void addMemories(MemoryKey key, List<Location> points) {
        memory.get(key).addAll(points);
    }

    @Override
    public Queue<Location> getMemories(MemoryKey key) {
        return memory.get(key);
    }
}
