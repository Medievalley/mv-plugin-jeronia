package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.Structure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModelImpl;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.events.StructsLoadedEvent;

import java.util.*;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final Map<String, ArrayList<Location>> structCorners;
    private final StructureContext structContext;
    private final Map<Integer, LoreStructure> structures;
    private final Map<String, Structure> selectedStruct;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin);
        this.structContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
        this.selectedStruct = new HashMap<>();

    }

    @Override
    public void create(
            User user, String type,
            String name, boolean destructible,
            IResultCallback cb
    ) throws Exception  {
        StructModelImpl m = new StructModelImpl();
        ArrayList<Location> corners = structCorners.get(user.getName());
        if (corners == null || corners.size() != 2) {
            throw new Exception("Structure location not set");
        }
        applyLocation(m, corners.get(0), corners.get(1));
        m.typeId = StructureType.valueOf(type.toUpperCase()).getTypeId();
        m.destructible = destructible;
        m.name = name;
        m.world = corners.get(0).getWorld().getName();

        int structId = structContext.save(m);
        if (structId != 0) {
            Optional<StructModel> model = structContext.getById(structId);
            model.ifPresent(this::registerStructure);
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    private void applyLocation(StructModelImpl m, Location l1, Location l2) {
        m.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        m.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        m.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        m.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        m.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    }

    public void setCorner(String key, Location l) {
        ArrayList<Location> corners = structCorners.computeIfAbsent(key, k -> new ArrayList<>());
        if (corners.size() == 2)
            corners.remove(0);
        corners.add(l);
    }

    public ArrayList<Location> getStructCorners(String key) {
        return structCorners.get(key);
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
    public void selectStructByLocation(String userName, Location l, IResultCallback cb) {
        Optional<LoreStructure> struct = this.getByLocation(l);
        if (struct.isPresent()) {
            Structure s = struct.get();
            selectedStruct.put(userName, s);
            cb.sendResult(true, String.format(
                    "Id: %d\n SizeX: %d\n SizeY: %d\n SizeZ: %d\n",
                    s.getId(),
                    s.getX2() - s.getX1() + 1,
                    s.getY2() - s.getY1() + 1,
                    s.getZ2() - s.getZ1() + 1)
            );
        } else {
            cb.sendResult(false, "This location is not part of any structure");
        }
    }

    @Override
    public String exportVolume(String userName, String volumeName) {
        Structure s = selectedStruct.get(userName);
        if (s != null) {
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
            int volumeId = structContext.createVolume(v, volumeBlocks);
            return String.format("VolumeId: %d", volumeId);
        } else {
            return "Error occurred";
        }
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

