package org.shrigorevich.ml.domain.services;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.shrigorevich.ml.db.models.Volume;
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
        System.out.println(type.toUpperCase()); //TODO: remove
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
        structureContext.saveAsync(m, cb);
    }

    private void applyLocation(CreateStructModel m, Location l1, Location l2) {
        m.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        m.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        m.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        m.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        m.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    };

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

    public IStructure registerStructure(GetStructModel m) { //TODO: error handling
        IStructure s = new Structure(m);
        structures.put(s.getId(), s);
        return s; //TODO: verify that struct in hashmap is also updated
    }

    public void getByIdAsync(int id) {
        structureContext.getByIdAsync(id, (model) -> {
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
        });
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
            structureContext.saveStructVolumeAsync(v, blockList, (res, volumeId) -> {
                cb.sendResult(res, String.format("VolumeId: %d", volumeId));
            });
        } else {
            cb.sendResult(false, ChatColor.YELLOW + "Please choose struct by right click to any struct block");
        }
    }

    public void applyVolumeToStruct(int structId, int volumeId, IResultCallback cb) {
        Optional<IStructure> struct = this.getRegisteredStructById(structId);
        if (struct.isPresent()) {
            structureContext.getVolumeByIdAsync(volumeId, (res, volumeBlocks) -> {
                if (res && volumeBlocks.size() > 0) {
                    List<Block> structBlocks = struct.get().getBlocks();
                    for(int i = 0; i < structBlocks.size(); i ++) {
                        Material type = Material.valueOf(volumeBlocks.get(i).getType());
                        structBlocks.get(i).setType(type);
                    }
                } else {
                    cb.sendResult(false, "Volume not found");
                }
            });
        } else {
            cb.sendResult(false, String.format("Stuct %s not found", structId));
        }
    }

    @Deprecated
    public void createDefault(User user, IResultCallback cb) throws Exception {
        CreateStructModel m = new CreateStructModel();
        ArrayList<Location> corners = structCorners.get(user.getName());
        if (corners == null || corners.size() != 2) {
            throw new Exception("Structure location not set");
        }
        applyLocation(m, corners.get(0), corners.get(1));
        m.typeId = StructureType.DEFAULT.getTypeId();
        m.destructible = false;
        m.name = String.format("%s`s structure", user.getName());
        m.ownerId = user.getId();
        m.world = corners.get(0).getWorld().getName();

        saveStruct(m, (model, result, msg) -> {
            if (result && model.isPresent()) {
                IStructure s = registerStructure(model.get());
                cb.sendResult(true, "gg"); //TODO: refactor message
            } else {
                cb.sendResult(false, "gg"); //TODO: refactor message
            }
        });
    }
}

