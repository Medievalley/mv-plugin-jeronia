package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;
import java.util.Optional;

public interface StructureService extends Service {

    void load();
    Optional<Structure> getById(int id);
    Optional<Structure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);
    void exportVolume(Structure structure, String volumeName, MsgCallback cb);
    void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb);
}
