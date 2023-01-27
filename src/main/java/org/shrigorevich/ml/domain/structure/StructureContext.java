package org.shrigorevich.ml.domain.structure;
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
    List<VolumeBlockModel> getVolumeBlocks(int id) throws Exception;
    Optional<VolumeModel> getVolumeById(int id);
    List<StructModel> getStructures() throws Exception;
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void attachVolume(int structId, int volumeId) throws Exception;
    void saveStructBlocks(List<StructBlockModel> blocks) throws Exception;
    @Deprecated
    Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId);

    /**
     * Deprecated: not use case
     * @param id - block identifier
     * @return - Optional of StructBlockModel
     */
    @Deprecated
    Optional<StructBlockModel> getStructBlock(int id);
    List<StructBlockModel> getStructBlocks(int structId) throws Exception;
    List<StructBlockModel> getStructBlocks() throws Exception;

    /**
     * Deprecated: not use case
     * @param structId - struct identifier
     * @return Number of broken struct blocs
     * @throws Exception - db script execution error
     */
    @Deprecated
    int getBrokenBlocksCount(int structId) throws Exception;
    void updateBlocksStatus(List<StructBlock> blocks, boolean isBroken) throws Exception;
    void restoreBlock(int id) throws Exception;
    void restoreStruct(int structId) throws Exception;
    void detachVolume(int structId);
    void updateResources(int structId, int stockSize);
}
