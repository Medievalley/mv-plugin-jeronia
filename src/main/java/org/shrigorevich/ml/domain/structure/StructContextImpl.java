package org.shrigorevich.ml.domain.structure;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.plugin.Plugin;
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
import java.util.logging.Logger;

public class StructContextImpl implements StructureContext { //TODO: extends abstract context

    private final Plugin plugin;
    private final DataSource dataSource;
    private final StructureQueryBuilderImpl structQueryBuilder;
    private final VolumeQueryBuilderImpl volumeQueryBuilder;
    private final Logger logger;

    public StructContextImpl(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.structQueryBuilder = new StructureQueryBuilderImpl();
        this.volumeQueryBuilder = new VolumeQueryBuilderImpl();
        this.logger = Logger.getLogger("StructContextImpl");
    }

    public int save(StructModel st) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            return run.insert(structQueryBuilder.save(st), h);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveStructure: " + ex);
            return 0;
        }
    }

    public Optional<StructModel> getById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<StructModelImpl> h = new BeanHandler(StructModelImpl.class);
            StructModelImpl s = run.query(structQueryBuilder.getById(id), h);
            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetById: " + ex);
            return Optional.empty();
        }
    }

    public int createVolume(VolumeModel v, List<VolumeBlockModel> volumeBlocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
            int volumeId = run.query(volumeQueryBuilder.create(v), volumeHandler);
            Object[][] volumeBlockValues = new Object[volumeBlocks.size()][6];

            for (int i = 0; i < volumeBlocks.size(); i++) {
                VolumeBlockModel b = volumeBlocks.get(i);
                volumeBlockValues[i] = new Object[] {
                        volumeId,
                        b.getType(),
                        b.getBlockData(),
                        b.getX(), b.getY(), b.getZ()
                };
            }
            run.batch(volumeQueryBuilder.createVolumeBlockBatch(), volumeBlockValues);
            return volumeId;

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveStructVolume: "  + ex);
            return 0;
        }
    }

    public List<VolumeBlockModel> getVolumeBlocks(int volumeId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<VolumeBlockModel>> volumeHandler = new BeanListHandler(VolumeBlockModelImpl.class);
            return run.query(volumeQueryBuilder.getBlocks(volumeId), volumeHandler);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
            return new ArrayList<>(0);
        }
    }

    public List<StructModel> getStructures() {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<StructModel>> h = new BeanListHandler(StructModelImpl.class);
            List<StructModel> structs = run.query(structQueryBuilder.getLoreStructures(), h);
            for (StructModel s : structs) {
                logger.info(String.format("Id: %d, TypeId: %d, Name: %s, VolumeId: %d brokenBlocks: %d%n",
                        s.getId(), s.getTypeId(), s.getName(), s.getVolumeId(), s.getBrokenBlocks()));
            }
            return structs;

        } catch (SQLException ex) {
            logger.severe(ex.toString());
            return new ArrayList<>(0);
        }
    }

    public void setStructVolume(int structId, int volumeId) {
        try {
            logger.info(String.format("Set volume: %d, %d%n", structId, volumeId));
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE lore_struct SET volume_id = %d where struct_id = %d", volumeId, structId);
            run.update(sql);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }

    public Optional<VolumeModel> getVolumeById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<VolumeModel> h = new BeanHandler(VolumeModelImpl.class);
            String sql = String.format("SELECT id, size_x as sizex, size_y as sizey, size_z as sizez, name \n" +
                    "FROM volume WHERE id=%d", id);

            VolumeModel volume = run.query(sql, h);
            return volume == null ? Optional.empty() : Optional.of(volume);

        } catch (SQLException ex) {
            logger.severe(ex.toString());
            return Optional.empty();
        }
    }

    public void saveStructBlocks(List<StructBlockModel> blocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] brokenBlockValues = new Object[blocks.size()][3];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockModel b = blocks.get(i);
                brokenBlockValues[i] = new Object[] {
                        b.getStructId(),
                        b.getVolumeBlockId(),
                        b.isTriggerDestruction()
                };
            }
            String sql = "INSERT INTO struct_block (struct_id, volume_block_id, trigger_destruction) VALUES (?, ?, ?)";
            int rows[] = run.batch(sql, brokenBlockValues);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }

    public List<StructBlockModel> getStructBlocks(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<StructBlockModel>> h = new BeanListHandler(StructBlockModelImpl.class);
            String sql = String.format(
                    "select b.id, s.id as structId, v.id as volumeBlockId, v.type, v.block_data as blockData, b.broken, b.trigger_destruction as triggerDestruction,\n" +
                    "v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z\n" +
                    "from struct_block b \n" +
                    "join volume_block v ON b.volume_block_id=v.id\n" +
                    "join struct s ON b.struct_id = s.id\n" +
                    "where struct_id=%d", structId);

            return run.query(sql, h);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
            return new ArrayList<>(0);
        }
    }

    //TODO: Maybe create new db index
    public Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
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
            logger.severe(ex.toString());
            return Optional.empty();
        }
    }

    public int updateBlocksStatus(List<StructBlockModel> blocks, boolean isBroken) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] blockValues = new Object[blocks.size()][2];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockModel b = blocks.get(i);
                blockValues[i] = new Object[] {
                        isBroken,
                        b.getId()
                };
            }
            String sql = "UPDATE struct_block SET broken=? WHERE id=?";
            int rows[] = run.batch(sql, blockValues);
            return rows.length;
        } catch (SQLException ex) {
            logger.severe(ex.toString());
            return 0;
        }
    }

    @Override
    public void restoreBlock(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE struct_block SET broken=false WHERE id=%d", id);
            run.update(sql);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }

    @Override
    public void restore(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE struct_block SET broken=false WHERE struct_id=%d and broken=true", structId);
            run.update(sql);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }

    @Override
    public void removeVolume(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql1 = String.format("UPDATE lore_struct SET volume_id=null WHERE struct_id=%d", structId);
            String sql2 = String.format("DELETE FROM struct_block WHERE struct_id=%d", structId);
            run.update(sql1);
            run.update(sql2);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }

    @Override
    public void updateStock(int structId, int stockSize) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE lore_struct SET stock=%d WHERE struct_id=%d", stockSize, structId);
            run.update(sql);
        } catch (SQLException ex) {
            logger.severe(ex.toString());
        }
    }
}

