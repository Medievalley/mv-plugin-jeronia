package org.shrigorevich.ml.domain.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.shrigorevich.ml.db.models.Volume;
import org.shrigorevich.ml.db.models.VolumeBlock;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.callbacks.ISaveStructCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.db.models.GetStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.IStructure;
import org.shrigorevich.ml.domain.models.Structure;
import org.shrigorevich.ml.domain.models.User;

import java.util.*;

public class StructureService implements IStructureService {
    private final Map<String, ArrayList<Location>> structCorners;
    private final IStructureContext structureContext;
    private final Map<Integer, IStructure> structures;
    private final Map<String, IStructure> selectedStruct;

    public StructureService(IStructureContext structureContext) {
        this.structureContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
        this.selectedStruct = new HashMap<>();
    }

    public void create(
            User user,
            String type,
            String name,
            boolean destructible,
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

        saveStruct(m, (model, result, msg) -> {
            if (result && model.isPresent()) {
                IStructure s = registerStructure(model.get());
                cb.sendResult(true, String.format("Structure registered. Id: %d", s.getId()));
            } else {
                cb.sendResult(false, "Something was wrong");
            }
        });
    }

    public void saveStruct(CreateStructModel m, ISaveStructCallback cb) {
        structureContext.save(m, cb);
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

    public IStructure registerStructure(GetStructModel m) {
        IStructure s = new Structure(m);
        structures.put(s.getId(), s);
        return s;
    }

    public void getByIdAsync(int id) {
        Optional<GetStructModel> model = structureContext.getById(id);

        if (model.isPresent()) {
            GetStructModel m = model.get();
            System.out.println(String.format("Model: %d, %s, %s, %b, %s, %d, %d",
                    m.getId(),
                    m.getName(),
                    m.getWorld(),
                    m.isDestructible(),
                    m.getOwner(),
                    m.getTypeId(),
                    m.getX1()));

            IStructure s = registerStructure(m);
            List<Block> blockList = s.getBlocks();
            System.out.println(blockList.size());
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
            List<Block> blockList = s.getBlocks();
            Volume v = new Volume(
                    volumeName,
                    s.getX2() - s.getX1() + 1,
                    s.getY2() - s.getY1() + 1,
                    s.getZ2() - s.getZ1() + 1
            );
            structureContext.saveStructVolume(v, blockList,
                    (res, volumeId) -> cb.sendResult(res, String.format("VolumeId: %d", volumeId)));
        } else {
            cb.sendResult(false, "Please choose struct by right click to any struct block");
        }
    }

    public void applyVolumeToStruct(int structId, int volumeId, IResultCallback cb) throws IllegalArgumentException {

        Optional<IStructure> struct = this.getRegisteredStructById(structId);
        if (!struct.isPresent()) throw new IllegalArgumentException(String.format("Stuct %d not found", structId));

        Optional<Volume> volume = structureContext.getVolumeById(volumeId);
        if (!volume.isPresent()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

        if(!isSizeEqual(struct.get(), volume.get()))
            throw new IllegalArgumentException("Structure and volume sizes are not equal");

        structureContext.setStructVolume(structId, volumeId);

        List<VolumeBlock> volumeBlocks = structureContext.getVolumeBlocks(volumeId);

        List<Block> structBlocks = struct.get().getBlocks();
        for(int i = 0; i < volumeBlocks.size(); i ++) {
            VolumeBlock vb = volumeBlocks.get(i);
            BlockData bd = Bukkit.createBlockData(vb.getBlockData());
            structBlocks.get(i).setBlockData(bd);
        }
    }

    public void loadStructures() {
        List<GetStructModel> structs = structureContext.getStructures();
        for (GetStructModel s : structs) {
            registerStructure(s);
        }
        Bukkit.getLogger().info("Loaded structs number: " + structures.size());
    }

//    public void ProcessExplodedBlockAsync(Block block) {
//        Optional<IStructure> struct = getByLocation(block.getLocation());
//        if (struct.isPresent() && struct.get().getV) {
//            Optional<>
//        }
//        if (struct.get().getType() != StructureType.PRIVATE) {
//            structureContext.sa
//        }
//    }
    private boolean isSizeEqual(IStructure struct, Volume volume) {
        return struct.getSizeX() == volume.getSizeX() &&
                struct.getSizeY() == volume.getSizeY() &&
                struct.getSizeZ() == volume.getSizeZ();
    }
}

