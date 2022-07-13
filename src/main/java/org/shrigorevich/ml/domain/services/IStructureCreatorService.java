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

public interface IStructureCreatorService {

    void applyLocation(CreateStructModel m, Location l1, Location l2);

    void setCorner(String key, Location l);

    ArrayList<Location> getStructCorners(String key);

    void saveStruct(CreateStructModel m, ICreateOneCallback cb);

    CreateStructModel getStruct(String key);

    void addStruct(CreateStructModel m, String key);
    void createDefault(User user, ICreateOneCallback cb) throws IllegalArgumentException;
}
