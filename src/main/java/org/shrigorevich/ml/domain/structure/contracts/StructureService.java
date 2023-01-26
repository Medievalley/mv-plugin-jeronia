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
    List<Structure> getDamagedStructs();
    Optional<Structure> getByLocation(Location location);
    void setBlocksBroken(List<StructBlockModel> blocks);
    void exportVolume(Structure structure, String volumeName, MsgCallback cb);
    void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb);
    void restoreBlock(StructBlockModel block) throws Exception;

     /**
     * Restore and reload struct by passed ID
     * @param structId - structure identifier
     */
    void restore(int structId);
    Optional<StructBlockModel> getStructBlock(int x, int y, int z);
    Optional<StructBlockModel> getStructBlock(Location l);
    @Deprecated
    int getBrokenBlocksCount(int structId) throws Exception;
    @Deprecated
    int getStructBlocksCount(int structId) throws Exception;

    /**
     * Assign a volume to the structure and immediately restore it to match the new volume
     * @param struct - TownInfra structure that support volume functionality
     * @param volumeId - volume identifier to be applied
     * @throws IllegalArgumentException -
     * 1. When the volume with provided ID does not exist.
     * 2. When struct and volume sizes do not match (x, y, z)
     *
     */
    void applyVolume(@NotNull TownInfra struct, int volumeId) throws IllegalArgumentException;
}
