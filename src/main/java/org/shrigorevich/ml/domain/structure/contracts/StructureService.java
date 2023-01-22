package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.models.UserModelImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StructureService extends Service {

    void load();
    Optional<LoreStructure> getById(int id);
    Optional<LoreStructure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);

    String exportVolume(String userName, String volumeName);
    void setCorner(String key, Location l);
    ArrayList<Location> getStructCorners(String key);
    void create(String name, StructureType type, Location l1, Location l2, IResultCallback cb);
    void selectStructByLocation(String userName, Location l, IResultCallback cb);

}
