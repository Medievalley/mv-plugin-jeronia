package org.shrigorevich.ml.domain.structure;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.common.CoordsImpl;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.Structure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModelImpl;
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

    @Override
    public Optional<Structure> getById (int id) {
        LoreStructure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    @Override
    public Optional<Structure> getByLocation(Location l) {
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

    //TODO: Remove StructModelImpl dependency
    @Override
    public void create(String name, StructureType type, Location l1, Location l2, IResultCallback cb) {
        StructModelImpl m = new StructModelImpl();
        m.typeId = type.getTypeId();
        m.name = name;
        m.world = l1.getWorld().getName();

        int structId = structContext.save(m);
        if (structId != 0) {
            Optional<StructModel> model = structContext.getById(structId);
            model.ifPresent(this::registerStructure);
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    //TODO: error handling required
    @Override
    public int exportVolume(Structure s, String volumeName) {
        VolumeModelImpl v = new VolumeModelImpl(
                volumeName,
                s.getSizeX(),
                s.getSizeY(),
                s.getSizeZ()
        );

        List<VolumeBlockModel> volumeBlocks = new ArrayList<>();
        List<Block> blocks = s.getBlocks();
        int offsetX = blocks.get(0).getX();
        int offsetY = blocks.get(0).getY();
        int offsetZ = blocks.get(0).getZ();

        for (Block b : blocks) {
            if (!b.getType().isAir()) {
                volumeBlocks.add(
                    new VolumeBlockModelImpl(
                        b.getX() - offsetX,
                        b.getY() - offsetY,
                        b.getZ() - offsetZ,
                        b.getType().toString(),
                        b.getBlockData().getAsString()
                    ));
            }
        }
        return structContext.createVolume(v, volumeBlocks);
    }

    //TODO: move to right place
    public Coords getMinCoords(Location l1, Location l2) {
        return new CoordsImpl(
                Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()),
                Math.min(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    public Coords getMaxCoords(Location l1, Location l2) {
        return new CoordsImpl(
                Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()),
                Math.max(l1.getBlockZ(), l2.getBlockZ())
        );
    }
}

