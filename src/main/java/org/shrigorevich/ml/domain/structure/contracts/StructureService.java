package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StructureService extends Service {

    void create(User user, String type, String name, boolean destructible, IResultCallback cb) throws Exception;
    void setCorner(String key, Location l);
    ArrayList<Location> getStructCorners(String key);
    String exportVolume(String userName, String volumeName);
    void selectStructByLocation(String userName, Location l, IResultCallback cb);
    void load();
    Optional<LoreStructure> getById(int id);
    Optional<LoreStructure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);
}
