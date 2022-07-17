package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.bukkit.block.Block;
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
    private Map<String, CreateStructModel> createList;
    private Map<Integer, IStructure> structures;

    public StructureService(IStructureContext structureContext) {
        this.structureContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
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
                cb.sendResult(true, "gg"); //TODO: refactor message
            } else {
                cb.sendResult(false, "gg"); //TODO: refactor message
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

    public IStructure registerStructure(GetStructModel m) { //TODO: error handling
        IStructure s = new Structure(m);
        structures.put(s.getId(), s);
        return s; //TODO: verify that struct in hashmap is also updated
    }

    public void getByIdAsync(int id) {
        structureContext.getByIdAsync(id, (model, msg) -> {
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
}
