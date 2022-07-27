package org.shrigorevich.ml.domain.services;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.models.*;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.structures.LoreStructImpl;
import org.shrigorevich.ml.domain.structures.LoreStructure;
import org.shrigorevich.ml.domain.structures.Structure;
import org.shrigorevich.ml.domain.users.User;

import java.util.*;

public class StructureService implements IStructureService {
    private final Map<String, ArrayList<Location>> structCorners;
    private final IStructureContext structContext;
    private final Map<Integer, Structure> structures;
    private final Map<String, Structure> selectedStruct;
    private final Plugin plugin;

    public StructureService(IStructureContext structureContext, Plugin plugin) {
        this.plugin = plugin;
        this.structContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
        this.selectedStruct = new HashMap<>();
    }

    public void create(
            User user, String type,
            String name, boolean destructible,
            IResultCallback cb
    ) throws Exception  {
        LoreStructModel m = new LoreStructModel();
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
            Optional<LoreStructModel> model = structContext.getById(structId);
            if (model.isPresent()) registerStructure(new LoreStructImpl(model.get(), structContext));
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    private void applyLocation(LoreStructModel m, Location l1, Location l2) {
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

    private void registerStructure(Structure s) {
        structures.put(s.getId(), s);
    }
    public Optional<Structure> getById (int id) {
        Structure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    private Optional<Structure> getByLocation(Location l) {
        for (Structure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public void selectStructByLocation(String userName, Location l, IResultCallback cb) {
        Optional<Structure> struct = this.getByLocation(l);
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

    public void saveStructVolume(String userName, String volumeName, IResultCallback cb) {
        Structure s = selectedStruct.get(userName);
        if (s != null) {
            Volume v = new Volume(
                    volumeName,
                    s.getSizeX(),
                    s.getSizeY(),
                    s.getSizeZ()
            );

            List<VolumeBlock> volumeBlocks = new ArrayList<>();
            List<Block> blocks = s.getBlocks();
            int offsetX = blocks.get(0).getX();
            int offsetY = blocks.get(0).getY();
            int offsetZ = blocks.get(0).getZ();

            for (Block b : blocks) {
                if (!b.getType().isAir()) {
                    volumeBlocks.add(
                            new VolumeBlock(
                                    b.getX() - offsetX,
                                    b.getY() - offsetY,
                                    b.getZ() - offsetZ,
                                    b.getType().toString(),
                                    b.getBlockData().getAsString()
                            ));
                }
            }
            structContext.createVolume(v, volumeBlocks,
                    (res, volumeId) -> cb.sendResult(res, String.format("VolumeId: %d", volumeId)));
        } else {
            cb.sendResult(false, "Please choose struct by right click to any struct block");
        }
    }
    public void applyVolumeToStruct(int structId, int volumeId, IResultCallback cb) throws IllegalArgumentException {

        Optional<Structure> struct = this.getById(structId);
        if (!struct.isPresent()) throw new IllegalArgumentException(String.format("Stuct %d not found", structId));

        Optional<Volume> volume = structContext.getVolumeById(volumeId);
        if (!volume.isPresent()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

        if(!isSizeEqual(struct.get(), volume.get()))
            throw new IllegalArgumentException("Structure and volume sizes are not equal");

        structContext.setStructVolume(structId, volumeId);
        Structure s = struct.get();
        List<VolumeBlock> volumeBlocks = structContext.getVolumeBlocks(volumeId); //TODO: Get Struct blocks
        List<StructBlock> structBlocks = new ArrayList<>();
        for(int i = 0; i < volumeBlocks.size(); i ++) {
            VolumeBlock vb = volumeBlocks.get(i);
            BlockData bd = Bukkit.createBlockData(vb.getBlockData());
            Block b = s.getWorld().getBlockAt(
                    vb.getX()+s.getX1(),
                    vb.getY()+s.getY1(),
                    vb.getZ()+s.getZ1());

            b.setBlockData(bd);
            structBlocks.add(new StructBlock(s.getId(), vb.getId(), false));
        }
        structContext.saveStructBlocks(structBlocks);
    }

    public void loadStructures() {
        List<LoreStructModel> structs = structContext.getStructures();
        for (LoreStructModel s : structs) {
            LoreStructure newStruct = new LoreStructImpl(s, structContext);
            registerStructure(newStruct);
        }
        Bukkit.getLogger().info("Loaded structs number: " + structures.size());
    }

    public void processExplodedBlocksAsync(List<Block> blocks) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<StructBlockFull> brokenBlocks = new ArrayList<>();
            for (Block block : blocks) {
                Optional<StructBlockFull> b = getBrokenBlock(block);
                b.ifPresent(brokenBlocks::add);
            }
            int count = structContext.updateStructBlocksBrokenStatus(brokenBlocks);
            System.out.println("Broken blocks count: " + count); //TODO: comment

            if (count == brokenBlocks.size() && count > 0) {
                HashMap<Integer, Structure> damagedStructures = new HashMap<>();
                //TODO: create StructDamagedEvent
                for (StructBlockFull b : brokenBlocks) {
                    if(damagedStructures.get(b.getStructId()) == null) {
                        Structure s = structures.get(b.getStructId());
                        damagedStructures.put(s.getId(), s);
                    }
                }

                for (Structure s : damagedStructures.values()) {
                    List<StructBlockFull> structBlocks = structContext.getStructBlocks(s.getId());
                    long brokenCounter = 0;
                    for (StructBlockFull b : structBlocks) {
                        if (b.isBroken()) brokenCounter += 1;
                    }

                    long destroyedPercent = brokenCounter * 100 / structBlocks.size();
                    System.out.println(brokenCounter + " " + structBlocks.size());
                    System.out.printf("Destroyed percent %d%n", destroyedPercent);
                    Bukkit.getScheduler().runTask(plugin, () -> {}); //TODO: Maybe this can be improved (Call event)
                }
            }
        });
    }

    private Optional<StructBlockFull> getBrokenBlock(Block block) {
        Optional<Structure> struct = getByLocation(block.getLocation());

        if (struct.isPresent() && struct.get() instanceof LoreStructure) {
            System.out.println("Lore Struct");
            LoreStructure s = (LoreStructure) struct.get();
            int x = block.getX() - s.getX1();
            int y = block.getY() - s.getY1();
            int z = block.getZ() - s.getZ1();
            Optional<StructBlockFull> sb = structContext.getStructBlock(x, y, z, s.getVolumeId(), s.getId());
            if (sb.isPresent() && !sb.get().isBroken()) {
                sb.get().setBroken(true);
                return sb;
            }
        }
        return Optional.empty();
    }

    private boolean isSizeEqual(Structure struct, Volume volume) {
        return struct.getSizeX() == volume.getSizeX() &&
                struct.getSizeY() == volume.getSizeY() &&
                struct.getSizeZ() == volume.getSizeZ();
    }

}

