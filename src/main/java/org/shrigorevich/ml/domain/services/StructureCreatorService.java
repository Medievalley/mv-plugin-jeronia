package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.shrigorevich.ml.db.callbacks.ICreateOneCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StructureCreatorService implements IStructureCreatorService{
    private final Map<String, ArrayList<Location>> structCorners;
    private final IStructureContext structureContext;
    private Map<String, CreateStructModel> structs;

    public StructureCreatorService(IStructureContext structureContext) {
        this.structureContext = structureContext;
        this.structCorners = new HashMap<>();
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

    public void saveStruct(CreateStructModel m, ICreateOneCallback cb) {
        structureContext.saveAsync(m, cb);
    }

    public CreateStructModel getStruct(String key) {
        return structs.get(key);
    }

    public void addStruct(CreateStructModel m, String key) {
        structs.put(key, m);
    }

    public void createDefault(User user, ICreateOneCallback cb) throws IllegalArgumentException{
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
        saveStruct(m, cb);
    }
}
