package org.shrigorevich.ml.state.mobs;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.mobs.MemoryHolder;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.LocationMemoryUnit;

import java.util.*;

public class MemoryHolderImpl implements MemoryHolder {


    private final EnumMap<MemoryKey, List<LocationMemoryUnit>> memory;

    public MemoryHolderImpl() {
        this.memory = new EnumMap<>(MemoryKey.class);
        for (MemoryKey key : MemoryKey.values()) {
            memory.put(key, new ArrayList<>());
        }
    }

    @Override
    public void addMemory(MemoryKey key, Location point) {
        if (!contains(key, point))
            memory.get(key).add(new LocationMemoryUnitImpl(point));
    }

    @Override
    public void addMemories(MemoryKey key, List<Location> points) {
        for (Location l : points) {
            addMemory(key, l);
        }
    }

    @Override
    public List<LocationMemoryUnit> getMemories(MemoryKey key) {
        return memory.get(key);
    }

    private boolean contains(MemoryKey key, Location l) {
        for (LocationMemoryUnit unit : memory.get(key)) {
            if (unit.locationEquals(l)) return true;
        }
        return false;
    }

}
