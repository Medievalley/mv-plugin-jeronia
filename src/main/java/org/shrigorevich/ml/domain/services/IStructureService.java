package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.callbacks.IStructureCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.models.User;

import java.util.ArrayList;

public interface IStructureService {

    void applyLocation(CreateStructModel m, Location l1, Location l2);

    void setCorner(String key, Location l);

    ArrayList<Location> getStructCorners(String key);

    void saveStruct(CreateStructModel m, IStructureCallback cb);

    CreateStructModel getStruct(String key);

    void addStruct(CreateStructModel m, String key);
    void createDefault(User user, IResultCallback cb) throws IllegalArgumentException;
}
