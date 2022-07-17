package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.callbacks.ISaveStructCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.models.User;

import java.util.ArrayList;

public interface IStructureService {

    void create(User user, String type, String name, boolean destructible, IResultCallback cb) throws Exception;

    void setCorner(String key, Location l);

    ArrayList<Location> getStructCorners(String key);

    void saveStruct(CreateStructModel m, ISaveStructCallback cb);

    void createDefault(User user, IResultCallback cb) throws Exception;

    void getByIdAsync(int id);
}
