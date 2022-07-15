package org.shrigorevich.ml.domain.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.callbacks.IStructureCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.db.models.GetStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.IStructure;
import org.shrigorevich.ml.domain.models.Structure;
import org.shrigorevich.ml.domain.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void applyLocation(CreateStructModel m, Location l1, Location l2) {
        m.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        m.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        m.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        m.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.y2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.z2 = Math.max(l1.getBlockX(), l2.getBlockX());
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

    public void saveStruct(CreateStructModel m, IStructureCallback cb) {
        structureContext.saveAsync(m, cb);
    }

    public CreateStructModel getStruct(String key) {
        return createList.get(key);
    }

    public void addStruct(CreateStructModel m, String key) {
        createList.put(key, m);
    }

    public void createDefault(User user, IResultCallback cb) throws IllegalArgumentException{
        CreateStructModel m = new CreateStructModel();
        ArrayList<Location> corners = structCorners.get(user.getName());
        if (corners == null || corners.size() != 2) {
            throw new IllegalArgumentException("Structure location not set");
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
                List<Block> blockList = s.getBlocks();
                Bukkit.getLogger().info(ChatColor.GREEN + String.format("Structure volume: %d", blockList.size()));
            } else {
                cb.sendResult(false, "gg"); //TODO: refactor message
            }
        });
    }

    public IStructure registerStructure(GetStructModel m) { //TODO: error handling
        IStructure s = new Structure(m);
        structures.put(s.getId(), s);
        return s;
    }
}
