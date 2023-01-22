package org.shrigorevich.ml.domain.structure.contracts;
import org.shrigorevich.ml.common.Context;
import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.util.List;
import java.util.Optional;

public interface StructureContext extends Context {

    int save(String name, int typeId, String world, Coordinates l1, Coordinates l2) throws Exception;
    Optional<StructModel> getById(int id) throws Exception;
    int createVolume(String name, int sizeX, int sizeY, int sizeZ) throws Exception;
    void saveVolumeBlocks(int volumeId, List<VolumeBlockModel> blocks) throws Exception;
    List<VolumeBlockModel> getVolumeBlocks(int id);
    Optional<VolumeModel> getVolumeById(int id);
    List<StructModel> getStructures();
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
    Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId);
    void saveStructBlocks(List<StructBlockModel> blocks);
    List<StructBlockModel> getStructBlocks(int structId);
    Optional<StructBlockModel> getStructBlock(int id);
    void updateBlocksStatus(List<StructBlockModel> blocks, boolean isBroken);
    void restoreBlock(int id);
    void restoreStruct(int structId);
    void removeVolume(int structId);
    void updateResources(int structId, int stockSize);
}
