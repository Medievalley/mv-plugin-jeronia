package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.shrigorevich.ml.db.models.*;
import org.shrigorevich.ml.domain.callbacks.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StructureContext implements IStructureContext {

    private final Plugin plugin;
    private final DataSource dataSource;
    private final BukkitScheduler scheduler;

    public StructureContext(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.scheduler = Bukkit.getScheduler();
    }

    public int save(CreateStruct st) {
        try {

            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Integer> h = new ScalarHandler<>();

            String sql2 = String.format(
                    "INSERT INTO structures (name, type_id, owner_id, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                    "VALUES ('%s', %d, %d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                    "RETURNING id",
                    st.name, st.typeId, st.ownerId, st.destructible, st.world, st.x1, st.y1, st.z1, st.x2, st.y2, st.z2
            );
            return run.insert(sql2, h);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveStructure: " + ex);
            return 0;
        }
    }

    public Optional<GetStruct> getById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<GetStruct> h = new BeanHandler(GetStruct.class);
            String sql = String.format("select s.id, s.name, s.type_id as typeid, s.destructible, s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2, \n" +
                    "u.username as owner\n" +
                    "from structures s join users u on s.owner_id = u.id\n" +
                    "where s.id=%d;", id);

            GetStruct s = run.query(sql, h);

            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetById: " + ex);
            return Optional.empty();
        }
    }

    public void createVolume(Volume v, List<VolumeBlock> volumeBlocks, ISaveVolumeCallback cb) { //TODO: Move offset logic to service
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
                String sql = String.format(
                        "INSERT INTO volumes (name, size_x, size_y, size_z) VALUES ('%s', %d, %d, %d) returning id;",
                        v.getName(), v.getSizeX(), v.getSizeY(), v.getSizeZ());
                int volumeId = run.query(sql, volumeHandler);

                Object[][] volumeBlockValues = new Object[volumeBlocks.size()][6];

                for (int i = 0; i < volumeBlocks.size(); i++) {
                    VolumeBlock b = volumeBlocks.get(i);
                    volumeBlockValues[i] = new Object[] {
                            volumeId,
                            b.getType(),
                            b.getBlockData(),
                            b.getX(),
                            b.getY(),
                            b.getZ()
                        };
                }

                sql = "INSERT INTO volume_blocks (volume_id, type, block_data, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
                int rows[] = run.batch(sql, volumeBlockValues);
                System.out.println(rows.length);

                scheduler.runTask(plugin, () -> cb.volumeSaved(true, volumeId));

            } catch (SQLException ex) {
                plugin.getLogger().severe("StructContext. SaveStructVolume: "  + ex);
            }
        });
    }

    public List<VolumeBlock> getVolumeBlocks(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<VolumeBlock>> volumeHandler = new BeanListHandler(VolumeBlock.class);
            String sql = String.format("SELECT id, type, block_data as blockdata, x, y, z FROM volume_blocks where volume_id=%d;", id);
            List<VolumeBlock> blocks = run.query(sql, volumeHandler);
            return blocks;
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetVolumeBlocks: " + ex);
            return new ArrayList<>(0);
        }
    }

    public List<GetStruct> getStructures() {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<GetStruct>> h = new BeanListHandler(GetStruct.class);
            String sql = String.format(
                    "SELECT s.id, s.name, s.type_Id as typeid, s.volume_id as volumeid, s.destructible, " +
                    "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2, \n" +
                    "u.username as owner\n" +
                    "FROM structures s JOIN users u ON s.owner_id = u.id");

            List<GetStruct> structs = run.query(sql, h);

            return structs;

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructures: " + ex);
            return new ArrayList<>(0);
        }
    }

    public void setStructVolume(int structId, int volumeId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE structures SET volume_id = %d where id = %d", volumeId, structId);
            run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SetStructVolume: " + ex);
        }
    }

    public Optional<Volume> getVolumeById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Volume> h = new BeanHandler(Volume.class);
            String sql = String.format("SELECT id, size_x as sizex, size_y as sizey, size_z as sizez, name \n" +
                    "FROM volumes WHERE id=%d", id);

            Volume volume = run.query(sql, h);
            return volume == null ? Optional.empty() : Optional.of(volume);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetVolumeById: " + ex);
            return Optional.empty();
        }
    }

    public void saveStructBlocks(List<StructBlock> blocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] brokenBlockValues = new Object[blocks.size()][2];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlock b = blocks.get(i);
                brokenBlockValues[i] = new Object[] {
                        b.getStructId(),
                        b.getVolumeBlockId(),
                        b.isBroken()
                };
            }
            String sql = "INSERT INTO struct_blocks (struct_id, volume_block_id, broken) VALUES (?, ?, ?)";
            int rows[] = run.batch(sql, brokenBlockValues);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveBrokenBlocks: " + ex);
        }
    }

    //TODO: create index by volumeBlockId
    public List<StructBlockFull> getStructBlocks(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<StructBlockFull>> h = new BeanListHandler(StructBlockFull.class);
            String sql = String.format(
                    "select s.id, v.id as volumeblockid, v.type, v.block_data as blockdata, v.x, v.y, v.z, s.broken \n" +
                    "from struct_blocks s join volume_blocks v \n" +
                    "ON s.volume_block_id=v.id \n" +
                    "where struct_id=%d", structId);

            return run.query(sql, h);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
            return new ArrayList<>(0);
        }
    }

    public Optional<StructBlockFull> getStructBlock(int x, int y, int z, int volumeId, int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<StructBlockFull> h = new BeanHandler(StructBlockFull.class);
            String sql = String.format(
                    "select s.id, v.id as volumeblockid, v.type, v.block_data as blockdata, v.x, v.y, v.z, s.broken \n" +
                            "from struct_blocks s join volume_blocks v \n" +
                            "ON s.volume_block_id=v.id \n" +
                            "where v.x=%d and v.y=%d and v.z=%d and v.volume_id=%d and struct_id=%d",
                    x, y, z, volumeId, structId);

            StructBlockFull block = run.query(sql, h);
            return block == null ? Optional.empty() : Optional.of(block);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
            return Optional.empty();
        }
    }

    public int updateStructBlocksBrokenStatus(List<StructBlockFull> blocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] blockValues = new Object[blocks.size()][2];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockFull b = blocks.get(i);
                blockValues[i] = new Object[] {
                        b.getId(),
                        b.isBroken()
                };
            }
            String sql = "UPDATE struct_blocks SET broken=? WHERE id=?";
            int rows[] = run.batch(sql, blockValues);
            return rows.length;
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveBrokenBlocks: " + ex);
            return 0;
        }
    }
}

//System.out.println(
//        String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
//                m.getId(), m.getName(), m.getTypeId(), m.isDestructible(),
//                m.getOwner(), m.getWorld(), m.getVolumeId(),
//                m.getX1(), m.getY1(), m.getZ1(),
//                m.getX2(), m.getY2(), m.getZ2())
//);
