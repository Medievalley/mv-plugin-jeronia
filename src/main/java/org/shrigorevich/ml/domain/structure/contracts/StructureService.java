package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;
import java.util.Optional;

public interface StructureService extends Service {

    void load() throws Exception;
    Optional<Structure> getStruct(int id);
    List<TownInfra> getDamagedStructs();
    Optional<Structure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);
    void exportVolume(Structure structure, String volumeName, MsgCallback cb);
    void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb);
    void restoreBlock(StructBlockModel block) throws Exception;
    void restore(@NotNull TownInfra struct);
    Optional<StructBlockModel> getStructBlock(int x, int y, int z);
    Optional<StructBlockModel> getStructBlock(Location l);
    List<StructBlockModel> getStructBlocks(int structId);
    int getBrokenBlocksCount(int structId) throws Exception;
    int getStructBlocksCount(int structId) throws Exception;

    /**
     *
     * @param struct
     * @param volumeId
     * @throws IllegalArgumentException
     *
     * Assign a volume to the structure and immediately restore it to match the new volume
     */
    void applyVolume(@NotNull TownInfra struct, int volumeId) throws IllegalArgumentException;
}
