package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

import java.util.List;

public interface MemoryHolder {

    void addMemory(MemoryKey key, Location point);
    void addMemories(MemoryKey key, List<Location> points);
    List<LocationMemoryUnit> getMemories(MemoryKey key);
}
