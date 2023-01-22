package org.shrigorevich.ml.domain.structure;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.events.StructsLoadedEvent;

import java.util.*;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final StructureContext structContext;
    private final Map<Integer, LoreStructure> structures;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin, LogManager.getLogger("StructureServiceImpl"));
        this.structContext = structureContext;
        this.structures = new HashMap<>();

    }

    public Optional<LoreStructure> getById (int id) {
        LoreStructure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    @Override
    public Optional<LoreStructure> getByLocation(Location l) {
        for (LoreStructure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public void load() {
        List<StructModel> structs = structContext.getStructures();
        List<LoreStructure> damagedStructs = new ArrayList<>();
        for (StructModel s : structs) {
            LoreStructure struct = registerStructure(s);
            if (s.getBlocks() > 0 && s.getBrokenBlocks() > 0) {
                damagedStructs.add(struct);
            }
        }
        if (!damagedStructs.isEmpty()) {
            Bukkit.getScheduler().runTask(getPlugin(),
                () -> Bukkit.getPluginManager().callEvent(new StructsLoadedEvent(damagedStructs)));
        }
    }

    private LoreStructure registerStructure(StructModel s) {
        LoreStructure newStruct = new LoreStructImpl(s, structContext);
        structures.put(newStruct.getId(), newStruct);
        return newStruct;
    }

    @Override
    public void setBlocksBroken(List<StructBlockModel> blocks) {
        structContext.updateBlocksStatus(blocks, true);
    }
}

