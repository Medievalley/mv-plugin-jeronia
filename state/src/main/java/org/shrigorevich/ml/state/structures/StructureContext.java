package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.state.Context;
import org.shrigorevich.ml.state.structures.models.*;

import java.util.List;
import java.util.Optional;

public interface StructureContext extends Context {

    int save(String name, int typeId, String world, Coords l1, Coords l2) throws Exception;
    Optional<StructModel> getById(int id) throws Exception;
    int createVolume(String name, int sizeX, int sizeY, int sizeZ) throws Exception;
    void saveVolumeBlocks(int volumeId, List<DraftVolumeBlock> blocks) throws Exception;
    List<VolumeBlockModel> getVolumeBlocks(int id) throws Exception;
    Optional<VolumeModel> getVolumeById(int id);
    List<StructModel> getStructures() throws Exception;
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void attachVolume(int structId, int volumeId) throws Exception;
    void saveStructBlocks(List<DraftStructBlock> blocks) throws Exception;
    void changeStructBlockType(int blockId, int typeId) throws Exception;
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
    void updateBlocksStatus(int[] blockIds, boolean isBroken) throws Exception;
    void restoreBlock(int id) throws Exception;
    void restoreStruct(int structId) throws Exception;
    void detachVolume(int structId);
    void updateResources(int structId, int stockSize);
}
