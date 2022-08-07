package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.domain.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StructureService {

    void create(User user, String type, String name, boolean destructible, IResultCallback cb) throws Exception;
    void setCorner(String key, Location l);
    ArrayList<Location> getStructCorners(String key);
    void exportVolume(String userName, String volumeName, IResultCallback cb);
    void selectStructByLocation(String userName, Location l, IResultCallback cb);
    void load();
    void processExplodedBlocksAsync(List<Block> blocks);
    Optional<LoreStructure> getById(int id);
    Optional<Structure> getByLocation(Location location);
    void delete(int structId);
}
