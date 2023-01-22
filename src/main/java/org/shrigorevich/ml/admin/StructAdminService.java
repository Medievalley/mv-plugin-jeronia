package org.shrigorevich.ml.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.StructureType;

import java.util.ArrayList;

public interface StructAdminService extends Service {
    String exportVolume(String userName, String volumeName);
    void setCorner(String key, Location l);
    ArrayList<Location> getStructCorners(String key);
    void create(String name, StructureType type, Coords l1, Coords l2);
    void selectStructByLocation(String userName, Location l, IResultCallback cb);

}
