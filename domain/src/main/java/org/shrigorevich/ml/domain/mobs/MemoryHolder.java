package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

import java.util.List;
import java.util.Queue;

public interface MemoryHolder {

    void addMemory(MemoryKey key, Location point);
    void addMemories(MemoryKey key, List<Location> points);
    Queue<Location> getMemories(MemoryKey key);
}
