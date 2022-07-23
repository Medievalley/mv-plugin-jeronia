package org.shrigorevich.ml.domain.services;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.models.*;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.IStructure;
import org.shrigorevich.ml.domain.models.Structure;
import org.shrigorevich.ml.domain.models.User;
import java.util.*;

public class StructureService implements IStructureService {
    private final Map<String, ArrayList<Location>> structCorners;
    private final IStructureContext structContext;
    private final Map<Integer, IStructure> structures;
    private final Map<String, IStructure> selectedStruct;
    private final Plugin plugin;
    private final Map<Integer, ArmorStand> structStands;

    public StructureService(IStructureContext structureContext, Plugin plugin) {
        this.plugin = plugin;
        this.structContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
        this.selectedStruct = new HashMap<>();
        this.structStands = new HashMap<>();
    }

    public void create(
            User user, String type,
            String name, boolean destructible,
            IResultCallback cb
    ) throws Exception  {
        CreateStructModel m = new CreateStructModel();
        ArrayList<Location> corners = structCorners.get(user.getName());
        if (corners == null || corners.size() != 2) {
            throw new Exception("Structure location not set");
        }
        applyLocation(m, corners.get(0), corners.get(1));
        m.typeId = StructureType.valueOf(type.toUpperCase()).getTypeId();
        m.destructible = destructible;
        m.name = name;
        m.ownerId = user.getId();
        m.world = corners.get(0).getWorld().getName();

        int structId = saveStruct(m);
        if (structId != 0) {
            Optional<GetStructModel> model = structContext.getById(structId);
            if (model.isPresent()) registerStructure(model.get());
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    private int saveStruct(CreateStructModel m) {
        return structContext.save(m);
    }

    private void applyLocation(CreateStructModel m, Location l1, Location l2) {
        m.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        m.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        m.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        m.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        m.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    }

    public void setCorner(String key, Location l) {
        ArrayList<Location> corners = structCorners.get(key);
        if (corners == null) {
            corners = new ArrayList<>();
            structCorners.put(key, corners);
        }
        if (corners.size() == 2)
            corners.remove(0);
        corners.add(l);

    }

    public ArrayList<Location> getStructCorners(String key) {
        return structCorners.get(key);
    }

    private Optional<IStructure> registerStructure(GetStructModel m) {
        try {
            IStructure s = new Structure(m);
            structures.put(s.getId(), s);
            return Optional.of(s);
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().severe(ex.getMessage());
            return Optional.empty();
        }
    }
    private Optional<IStructure> getRegisteredStructById (int id) {
        IStructure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    private Optional<IStructure> getByLocation(Location l) {
        for (IStructure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public void selectStructByLocation(String userName, Location l, IResultCallback cb) {
        Optional<IStructure> struct = this.getByLocation(l);
        if (struct.isPresent()) {
            IStructure s = struct.get();
            selectedStruct.put(userName, s);
            cb.sendResult(true, String.format(
                    "Id: %d\n Name: %s\n SizeX: %d\n SizeY: %d\n SizeZ: %d\n",
                    s.getId(),
                    s.getName(),
                    s.getX2() - s.getX1() + 1,
                    s.getY2() - s.getY1() + 1,
                    s.getZ2() - s.getZ1() + 1)
            );
        } else {
            cb.sendResult(false, "This location is not part of any structure");
        }
    }

    public void saveStructVolume(String userName, String volumeName, IResultCallback cb) {
        IStructure s = selectedStruct.get(userName);
        if (s != null) {

            Volume v = new Volume(
                    volumeName,
                    s.getSizeX(),
                    s.getSizeY(),
                    s.getSizeZ()
            );

            List<VolumeBlock> volumeBlocks = new ArrayList<>(); //s.getBlocks();
            List<Block> structBlocks = s.getBlocks();
            int offsetX = structBlocks.get(0).getX();
            int offsetY = structBlocks.get(0).getY();
            int offsetZ = structBlocks.get(0).getZ();

            for (Block b : structBlocks) {
                volumeBlocks.add(
                    new VolumeBlock(
                        b.getX() - offsetX,
                        b.getY() - offsetY,
                        b.getZ() - offsetZ,
                        b.getType().toString(),
                        b.getBlockData().getAsString()
                    ));
            }
            structContext.saveStructVolume(v, volumeBlocks,
                    (res, volumeId) -> cb.sendResult(res, String.format("VolumeId: %d", volumeId)));
        } else {
            cb.sendResult(false, "Please choose struct by right click to any struct block");
        }
    }

    public void applyVolumeToStruct(int structId, int volumeId, IResultCallback cb) throws IllegalArgumentException {

        Optional<IStructure> struct = this.getRegisteredStructById(structId);
        if (!struct.isPresent()) throw new IllegalArgumentException(String.format("Stuct %d not found", structId));

        Optional<Volume> volume = structContext.getVolumeById(volumeId);
        if (!volume.isPresent()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

        if(!isSizeEqual(struct.get(), volume.get()))
            throw new IllegalArgumentException("Structure and volume sizes are not equal");

        structContext.setStructVolume(structId, volumeId);

        List<VolumeBlock> volumeBlocks = structContext.getVolumeBlocks(volumeId);

        List<Block> structBlocks = struct.get().getBlocks();
        for(int i = 0; i < volumeBlocks.size(); i ++) {
            VolumeBlock vb = volumeBlocks.get(i);
            BlockData bd = Bukkit.createBlockData(vb.getBlockData());
            structBlocks.get(i).setBlockData(bd);
        }
    }

    public void loadStructures() {
        List<GetStructModel> structs = structContext.getStructures();
        for (GetStructModel s : structs) {
            registerStructure(s);
        }
        Bukkit.getLogger().info("Loaded structs number: " + structures.size());
    }

    public void processExplodedBlocksAsync(List<Block> blocks) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<BrokenBlock> brokenBlocks = new ArrayList<>();
            for (Block block : blocks) {
                Optional<BrokenBlock> b = getBrokenBlock(block);
                b.ifPresent(brokenBlocks::add);
            }
            structContext.saveBrokenBlocks(brokenBlocks);

            HashMap<Integer, IStructure> damagedStructures = new HashMap<>();
            //TODO: create StructDamagedEvent
            for (BrokenBlock b : brokenBlocks) {
                IStructure s = structures.get(b.getStructId());
                int blocksByStruct = s.getBrokenBlocksNumber();
                if (blocksByStruct == 0)
                    s.setBrokenBlocks(1);
                else
                    s.setBrokenBlocks(blocksByStruct + 1);

                damagedStructures.put(s.getId(), s);
            }

            for (IStructure s : damagedStructures.values()) {
                long volumeBlocksCount = structContext.getVolumeNotAirBlocksNumber(s.getVolumeId());
                long destroyedPercent = s.getBrokenBlocksNumber() * 100L / volumeBlocksCount;
                System.out.println(s.getBrokenBlocksNumber() + " " + volumeBlocksCount);
                System.out.printf("Destroyed percent %d%n", destroyedPercent);
                Bukkit.getScheduler().runTask(plugin, () -> {}); //TODO: Maybe this can be improved (Call event)
            }

        });
    }

    private Optional<BrokenBlock> getBrokenBlock(Block block) {
        Optional<IStructure> struct = getByLocation(block.getLocation());

        if (struct.isPresent() && struct.get().getType() != StructureType.PRIVATE) {
            IStructure s = struct.get();
            int x = block.getX() - s.getX1();
            int y = block.getY() - s.getY1();
            int z = block.getZ() - s.getZ1();
            Optional<VolumeBlock> vb = structContext.getVolumeBlock(x, y, z, s.getVolumeId());
            if (vb.isPresent()) {
                VolumeBlock volumeBlock = vb.get();
                if(!Material.valueOf(volumeBlock.getType()).isAir()) {
                    return Optional.of(new BrokenBlock(volumeBlock.getId(), s.getId()));
                }
            }
        }
        return Optional.empty();
    }

    private boolean isSizeEqual(IStructure struct, Volume volume) {
        return struct.getSizeX() == volume.getSizeX() &&
                struct.getSizeY() == volume.getSizeY() &&
                struct.getSizeZ() == volume.getSizeZ();
    }

}

