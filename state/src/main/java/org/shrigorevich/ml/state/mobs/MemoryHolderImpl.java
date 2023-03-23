package org.shrigorevich.ml.state.mobs;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.mobs.MemoryHolder;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.MemoryUnit;

import java.util.*;

public class MemoryHolderImpl implements MemoryHolder {

    private final EnumMap<MemoryKey, List<MemoryUnit>> memory;
    private final Queue<Location> routePoints;

    public MemoryHolderImpl() {
        this.memory = new EnumMap<>(MemoryKey.class);
        this.routePoints = new LinkedList<>();
        for (MemoryKey key : MemoryKey.values()) {
            memory.put(key, new ArrayList<>());
        }
    }

    @Override
    public Queue<Location> getRoutePoints() {
        return routePoints;
    }

    @Override
    public void addRoutePoint(Location point) {
        this.routePoints.add(point);
    }

    @Override
    public void addRoutePoints(List<Location> points) {
        this.routePoints.addAll(points);
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
