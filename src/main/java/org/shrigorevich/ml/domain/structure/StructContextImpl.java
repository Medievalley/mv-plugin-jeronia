package org.shrigorevich.ml.domain.structure;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.models.StructBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;
import org.shrigorevich.ml.domain.volume.models.VolumeModelImpl;
import org.shrigorevich.ml.domain.structure.models.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public int save(StructModel st) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            return run.insert(structQueryBuilder.save(st), h);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return 0;
        }
    }

    public Optional<StructModel> getById(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructModelImpl> h = new BeanHandler(StructModelImpl.class);
            StructModelImpl s = run.query(structQueryBuilder.getById(id), h);
            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return Optional.empty();
        }
    }

    public int createVolume(VolumeModel v, List<VolumeBlockModel> volumeBlocks) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
            int volumeId = run.query(volumeQueryBuilder.create(v), volumeHandler);
            Object[][] volumeBlockValues = new Object[volumeBlocks.size()][6];

            for (int i = 0; i < volumeBlocks.size(); i++) {
                VolumeBlockModel b = volumeBlocks.get(i);
                volumeBlockValues[i] = new Object[] {
                    volumeId,
                    b.getMaterial(),
                    b.getBlockData(),
                    b.getX(), b.getY(), b.getZ()
                };
            }
            run.batch(volumeQueryBuilder.createVolumeBlockBatch(), volumeBlockValues);
            return volumeId;

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return 0;
        }
    }

    public List<VolumeBlockModel> getVolumeBlocks(int volumeId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<VolumeBlockModel>> volumeHandler = new BeanListHandler(VolumeBlockModelImpl.class);
            return run.query(volumeQueryBuilder.getBlocks(volumeId), volumeHandler);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return new ArrayList<>(0);
        }
    }

    public List<StructModel> getStructures() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructModel>> h = new BeanListHandler(StructModelImpl.class);
            List<StructModel> structs = run.query(structQueryBuilder.getStructures(), h);
            for (StructModel s : structs) {
                getLogger().info(String.format("Id: %d, TypeId: %d, Name: %s, VolumeId: %d brokenBlocks: %d%n",
                        s.getId(), s.getTypeId(), s.getName(), s.getVolumeId(), s.getBrokenBlocks()));
            }
            return structs;

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return new ArrayList<>(0);
        }
    }

    public void setStructVolume(int structId, int volumeId) {
        try {
            getLogger().info(String.format("Set volume: %d, %d%n", structId, volumeId));
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.setVolume(structId, volumeId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }

    public Optional<VolumeModel> getVolumeById(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<VolumeModel> h = new BeanHandler(VolumeModelImpl.class);
            VolumeModel volume = run.query(structQueryBuilder.getVolumeById(id), h);
            return volume == null ? Optional.empty() : Optional.of(volume);

        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return Optional.empty();
        }
    }

    public void saveStructBlocks(List<StructBlockModel> blocks) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            Object[][] brokenBlockValues = new Object[blocks.size()][3];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockModel b = blocks.get(i);
                brokenBlockValues[i] = new Object[] {
                    b.getStructId(),
                    b.getVolumeBlockId(),
                    b.isTriggerDestruction()
                };
            }
            run.batch(structQueryBuilder.saveStructBlocks(), brokenBlockValues);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }

    public List<StructBlockModel> getStructBlocks(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructBlockModel>> h = new BeanListHandler(StructBlockModelImpl.class);
            return run.query(structQueryBuilder.getStructBlocks(structId), h);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            return new ArrayList<>(0);
        }
    }

    @Override
    public Optional<StructBlockModel> getStructBlock(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructBlockModel> h = new BeanHandler(StructBlockModelImpl.class);
            StructBlockModel block = run.query(structQueryBuilder.getStructBlock(id), h);
            if (block == null) {
                getLogger().error(String.format("Struct block with id: %d not found", id));
            }
            return block == null ? Optional.empty() : Optional.of(block);
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            //TODO: throw exception instead of return value
            return Optional.empty();
        }
    }

    //TODO: Not use case. Should be replaced with getStructBlockById(int id);
    @Deprecated
    public Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructBlockModel> h = new BeanHandler(StructBlockModelImpl.class);
            String sql = String.format(
                    "select b.id, s.id as structId, v.id as volumeBlockId, v.type, v.block_data as blockData, b.broken, b.trigger_destruction as triggerDestruction,\n" +
                    "v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z\n" +
                    "from struct_block b \n" +
                    "join volume_block v ON b.volume_block_id=v.id\n" +
                    "join struct s ON b.struct_id = s.id\n" +
                    "where v.x=%d and v.y=%d and v.z=%d and v.volume_id=%d and struct_id=%d",
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

    public int updateBlocksStatus(List<StructBlockModel> blocks, boolean isBroken) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            Object[][] blockValues = new Object[blocks.size()][2];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockModel b = blocks.get(i);
                blockValues[i] = new Object[] {
                        isBroken,
                        b.getId()
                };
            }
            int[] rows = run.batch(structQueryBuilder.updateBlocksStatus(), blockValues);
            return rows.length;
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
            //TODO: Throw exception
            return 0;
        }
    }

    @Override
    public void restoreBlock(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.restoreBlock(id));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }

    @Override
    public void restoreStruct(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(structQueryBuilder.restoreStruct(structId));
        } catch (SQLException ex) {
            getLogger().error(ex.toString());
        }
    }

    @Override
    public void removeVolume(int structId) {
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

