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
    Optional<Structure> getById(int id);
    Optional<Structure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);

    int exportVolume(Structure structure, String volumeName);
    void create(String name, StructureType type, Location l1, Location l2, IResultCallback cb);
}
