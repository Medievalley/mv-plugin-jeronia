package org.shrigorevich.ml.domain.structure;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.common.CoordinatesImpl;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.Structure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.events.StructsLoadedEvent;

import java.util.*;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final StructureContext structContext;
    private final Map<Integer, FoodStructure> structures;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin, LogManager.getLogger("StructureServiceImpl"));
        this.structContext = structureContext;
        this.structures = new HashMap<>();
    }

    @Override
    public Optional<Structure> getById (int id) {
        FoodStructure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    @Override
    public Optional<Structure> getByLocation(Location l) {
        for (FoodStructure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public void load() {
        List<StructModel> structs = structContext.getStructures();
        List<FoodStructure> damagedStructs = new ArrayList<>();
        for (StructModel s : structs) {
            FoodStructure struct = registerStructure(s);
            if (s.getBlocks() > 0 && s.getBrokenBlocks() > 0) {
                damagedStructs.add(struct);
            }
        }
        if (!damagedStructs.isEmpty()) {
            Bukkit.getScheduler().runTask(getPlugin(),
                () -> Bukkit.getPluginManager().callEvent(new StructsLoadedEvent(damagedStructs)));
        }
    }

    private FoodStructure registerStructure(StructModel s) {
        FoodStructure newStruct = new FoodStructImpl(s, structContext);
        structures.put(newStruct.getId(), newStruct);
        return newStruct;
    }

    @Override
    public void setBlocksBroken(List<StructBlockModel> blocks) {
        structContext.updateBlocksStatus(blocks, true);
    }

    @Override
    public void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb) {
        try {
            int structId = structContext.save(
                    name, type.getTypeId(), l1.getWorld().getName(), getMinCoords(l1, l2), getMaxCoords(l1, l2));

            if (structId != 0) {
                Optional<StructModel> model = structContext.getById(structId);
                model.ifPresent(this::registerStructure);
                cb.result(String.format("StructId: %d", structId));
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            cb.result(e.getMessage());
        }
    }

    @Override
    public void exportVolume(Structure s, String volumeName, MsgCallback cb) {
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
        try {
            int volumeId = structContext.createVolume(volumeName, s.getSizeX(), s.getSizeY(), s.getSizeZ());
            structContext.saveVolumeBlocks(volumeId, volumeBlocks);
            cb.result(String.format("VolumeId: %d", volumeId));
        } catch (Exception ex) {
            cb.result(ex.getMessage());
        }
    }

    private Coordinates getMinCoords(Location l1, Location l2) {
        return new CoordinatesImpl(
            Math.min(l1.getBlockX(), l2.getBlockX()),
            Math.min(l1.getBlockY(), l2.getBlockY()),
            Math.min(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    private Coordinates getMaxCoords(Location l1, Location l2) {
        return new CoordinatesImpl(
            Math.max(l1.getBlockX(), l2.getBlockX()),
            Math.max(l1.getBlockY(), l2.getBlockY()),
            Math.max(l1.getBlockZ(), l2.getBlockZ())
        );
    }
}

