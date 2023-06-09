package org.shrigorevich.ml.database.structures;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.database.BaseContext;

import org.shrigorevich.ml.state.structures.*;
import org.shrigorevich.ml.state.structures.models.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StructContextImpl extends BaseContext implements StructureContext {

    private final StructureQueryBuilderImpl structQueryBuilder;
    private final VolumeQueryBuilderImpl volumeQueryBuilder;

    public StructContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("StructContextImpl"));
        this.structQueryBuilder = new StructureQueryBuilderImpl();
        this.volumeQueryBuilder = new VolumeQueryBuilderImpl();
    }

    @Override
    public int save(String name, int typeId, String world, Coords l1, Coords l2) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            return run.insert(structQueryBuilder.save(name, typeId, world, l1, l2), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error occurred while saving struct: " +
                String.format("{ name: %s, world: %s, typeId: %d, x1: %d, y1: %d, z1: %d, x2: %d, y2: %d, z2: %d }",
                name, world, typeId, l1.x(), l1.y(), l1.z(), l2.x(), l2.y(), l2.z()));
        }
    }

    @Deprecated //Not use case
    @Override
    public Optional<StructModel> getById(int id) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructModel> h = new BeanHandler<>(StructModelImpl.class);
            StructModel s = run.query(structQueryBuilder.getById(id), h);
            return s == null ? Optional.empty() : Optional.of(s);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while getting struct with id: %d", id));
        }
    }

    @Override
    public int createVolume(String name, int sizeX, int sizeY, int sizeZ) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
            return run.query(volumeQueryBuilder.create(name, sizeX, sizeY, sizeZ), volumeHandler);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(
                String.format("Error while creating volume: { name: %s, sizeX: %d, sizeY: %d, sizeZ: %d }",
                name, sizeX, sizeY, sizeZ));
        }
    }

    @Override
    public void saveVolumeBlocks(int volumeId, List<DraftVolumeBlock> volumeBlocks) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            Object[][] volumeBlockValues = new Object[volumeBlocks.size()][6];

            for (int i = 0; i < volumeBlocks.size(); i++) {
                DraftVolumeBlock b = volumeBlocks.get(i);
                volumeBlockValues[i] = new Object[] {
                    volumeId,
                    b.material(),
                    b.blockData(),
                    b.x(), b.y(), b.z()
                };
            }
            run.batch(volumeQueryBuilder.createVolumeBlockBatch(), volumeBlockValues);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while saving volume blocks");
        }
    }

    @Override
    public List<VolumeBlockModel> getVolumeBlocks(int volumeId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<VolumeBlockModel>> volumeHandler = new BeanListHandler<>(VolumeBlockModelImpl.class);
            return run.query(volumeQueryBuilder.getBlocks(volumeId), volumeHandler);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while getting blocks for volume: %d", volumeId));
        }
    }

    @Override
    public List<StructModel> getStructures() throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructModel>> h = new BeanListHandler<>(StructModelImpl.class);
            List<StructModel> structs = run.query(structQueryBuilder.getStructures(), h);
            for (StructModel s : structs) {
                getLogger().info(String.format("Id: %d, TypeId: %d, Name: %s, VolumeId: %d brokenBlocks: %d%n",
                        s.getId(), s.getTypeId(), s.getName(), s.getVolumeId(), s.getBrokenBlocks()));
            }
            return structs;

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception("Error while getting list of structures");
        }
    }

    @Override
    public void attachVolume(int structId, int volumeId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.setVolume(structId, volumeId));
            getLogger().info(String.format("Set volume: %d, %d%n", structId, volumeId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while attaching volume: %d to struct: %d", volumeId, structId));
        }
    }

    @Override
    public Optional<VolumeModel> getVolumeById(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<VolumeModel> h = new BeanHandler<>(VolumeModelImpl.class);
            VolumeModel volume = run.query(structQueryBuilder.getVolumeById(id), h);
            return volume == null ? Optional.empty() : Optional.of(volume);

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return Optional.empty();
        }
    }

    @Override
    public void saveStructBlocks(List<DraftStructBlock> blocks) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            Object[][] brokenBlockValues = new Object[blocks.size()][3];

            for (int i = 0; i < blocks.size(); i++) {
                DraftStructBlock b = blocks.get(i);
                brokenBlockValues[i] = new Object[] {
                    b.getStructId(),
                    b.getVolumeBlockId(),
                    b.isHealthPoint()
                };
            }
            run.batch(structQueryBuilder.saveStructBlocks(), brokenBlockValues);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception("Error while saving struct blocks");
        }
    }

    @Override
    public void changeStructBlockType(int blockId, int typeId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.changeBlockType(blockId, typeId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while changing block [id=%d] type to %d)", blockId, typeId));
        }
    }

    @Override
    public List<StructBlockModel> getStructBlocks(int structId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructBlockModel>> h = new BeanListHandler<>(StructBlockModelImpl.class);
            return run.query(structQueryBuilder.getStructBlocks(structId), h);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while getting blocks for struct: %d", structId));
        }
    }

    @Override
    public List<StructBlockModel> getStructBlocks() throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructBlockModel>> h = new BeanListHandler<>(StructBlockModelImpl.class);
            return run.query(structQueryBuilder.getStructBlocks(), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
             throw new Exception("Error while getting all struct blocks");
        }
    }

    @Deprecated // not use case
    @Override
    public Optional<StructBlockModel> getStructBlock(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            BeanHandler<StructBlockModel> h = new BeanHandler<>(StructBlockModelImpl.class);
            StructBlockModel block = run.query(structQueryBuilder.getStructBlock(id), h);
            if (block == null) {
                getLogger().error(String.format("Struct block with id: %d not found", id));
            }
            return block == null ? Optional.empty() : Optional.of(block);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return Optional.empty();
        }
    }

    //TODO: Not use case. Should be replaced with getStructBlockById(int id);
    @Deprecated
    public Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructBlockModel> h = new BeanHandler<>(StructBlockModelImpl.class);
            String sql = String.format(
                    """
                    select b.id, s.id as structId, v.id as volumeBlockId, v.type, v.block_data as blockData,
                    b.broken, b.trigger_destruction as triggerDestruction,
                    v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z
                    from struct_block b
                    join volume_block v ON b.volume_block_id=v.id
                    join struct s ON b.struct_id = s.id
                    where v.x=%d and v.y=%d and v.z=%d and v.volume_id=%d and struct_id=%d""",
                    x, y, z, volumeId, structId);

            StructBlockModel block = run.query(sql, h);
            if (block == null) {
                System.out.printf("Null block: %s, %s, %s, %s, %s%n", x, y, z, volumeId, structId);
            }
            return block == null ? Optional.empty() : Optional.of(block);

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return Optional.empty();
        }
    }

    @Override
    public void updateBlocksStatus(int[] blockIds, boolean isBroken) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            Object[][] blockValues = new Object[blockIds.length][2];

            for (int i = 0; i < blockIds.length; i++) {
                blockValues[i] = new Object[] {
                        isBroken,
                        blockIds[i]
                };
            }
            run.batch(structQueryBuilder.updateBlocksStatus(), blockValues);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception("Error while updating blocks broken status");
        }
    }

    public int getBrokenBlocksCount(int structId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            return run.query(structQueryBuilder.getBrokenBlockCount(structId), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while getting damaged struct ids");
        }
    }

    @Override
    public void restoreBlock(int id) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.restoreBlock(id));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while restoring block with id: %d", id));
        }
    }

    @Override
    public void restoreStruct(int structId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.restoreStruct(structId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            throw new Exception(String.format("Error while restoring struct: %d", structId));
        }
    }

    @Override
    public void detachVolume(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.unAttachVolume(structId));
            run.update(structQueryBuilder.clearStructBlocks(structId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }

    @Override
    public void updateResources(int structId, int stockSize) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.updateResources(structId, stockSize));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }
}

