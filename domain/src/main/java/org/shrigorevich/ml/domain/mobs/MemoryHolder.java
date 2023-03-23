package org.shrigorevich.ml.domain.mobs;

import org.bukkit.Location;

import java.util.List;
import java.util.Queue;

public interface MemoryHolder {

    Queue<Location> getRoutePoints();
    void addRoutePoint(Location point);
    void addRoutePoints(List<Location> points);
    List<MemoryUnit> getMemory(MemoryKey key);

    void addMemory(MemoryUnit unit);

}
