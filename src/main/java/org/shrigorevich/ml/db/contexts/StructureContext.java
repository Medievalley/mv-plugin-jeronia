package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.shrigorevich.ml.db.models.Volume;
import org.shrigorevich.ml.db.models.VolumeBlock;
import org.shrigorevich.ml.domain.callbacks.*;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.db.models.GetStructModel;

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

    public void save(CreateStructModel st, ISaveStructCallback cb) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {

                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<GetStructModel> h = new BeanHandler(GetStructModel.class);
                GetStructModel m = run.insert(String.format(
                        "INSERT INTO structures (name, type_id, owner_id, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                        "VALUES ('%s', %d, %d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                        "RETURNING *",
                        st.name, st.typeId, st.ownerId, st.destructible, st.world, st.x1, st.y1, st.z1, st.x2, st.y2, st.z2), h);

                scheduler.runTask(plugin, () -> cb.done(Optional.of(m), true, "Ok"));

            } catch (SQLException ex) {
                plugin.getLogger().severe(ex.toString());
                cb.done(Optional.empty(), false, ex.toString());
            }
        });
    }

    public Optional<GetStructModel> getById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<GetStructModel> h = new BeanHandler(GetStructModel.class);
            String sql = String.format("select s.id, s.name, s.type_id as typeid, s.destructible, s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2, \n" +
                    "u.username as owner\n" +
                    "from structures s join users u on s.owner_id = u.id\n" +
                    "where s.id=%d;", id);

            GetStructModel s = run.query(sql, h);

            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext:90. Exception: " + ex.toString());
            return Optional.empty();
        }
    }

    public void saveStructVolume(Volume v, List<Block> blockList, ISaveVolumeCallback cb) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
                String sql = String.format(
                        "INSERT INTO volumes (name, size_x, size_y, size_z) VALUES ('%s', %d, %d, %d) returning id;",
                        v.getName(), v.getSizeX(), v.getSizeY(), v.getSizeZ());
                int volumeId = run.query(sql, volumeHandler);



                Object[][] volumeBlockValues = new Object[blockList.size()][6];
                int offsetX = blockList.get(0).getX();
                int offsetY = blockList.get(0).getY();
                int offsetZ = blockList.get(0).getZ();
                for (int i = 0; i < blockList.size(); i++) {
                    Block b = blockList.get(i);
                    volumeBlockValues[i] = new Object[] {
                            volumeId,
                            b.getType().toString(),
                            b.getBlockData().getAsString(),
                            b.getX() - offsetX,
                            b.getY() - offsetY,
                            b.getZ() - offsetZ,
                        };
                }

                sql = "INSERT INTO volume_blocks (volume_id, type, block_data, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
                int rows[] = run.batch(sql, volumeBlockValues);
                System.out.println(rows.length);

                scheduler.runTask(plugin, () -> cb.volumeSaved(true, volumeId));

            } catch (SQLException ex) {
                plugin.getLogger().severe("StructContext: Exception: " + ex);
            }
        });
    }

    public List<VolumeBlock> getVolumeBlocks(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<VolumeBlock>> volumeHandler = new BeanListHandler(VolumeBlock.class);
            String sql = String.format("SELECT type, block_data as blockdata, x, y, z FROM volume_blocks where volume_id=%d;", id);
            List<VolumeBlock> blocks = run.query(sql, volumeHandler);
            return blocks;
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext: Exception: " + ex);
            return new ArrayList<>(0);
        }
    }

    public List<GetStructModel> getStructures() {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<GetStructModel>> h = new BeanListHandler(GetStructModel.class);
            String sql = String.format(
                    "SELECT s.id, s.name, s.type_Id as typeid, s.volume_id as volumeid, s.destructible, " +
                    "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2, \n" +
                    "u.username as owner\n" +
                    "FROM structures s JOIN users u ON s.owner_id = u.id");

            List<GetStructModel> structs = run.query(sql, h);

            return structs;

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext: Exception: " + ex.getMessage());
            return new ArrayList<>(0);
        }
    }

    public void setStructVolume(int structId, int volumeId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE structures SET volume_id = %d where id = %d", volumeId, structId);
            int rows = run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext: Exception: " + ex.getMessage());
        }
    }

    public void saveBrokenBlock(Block block, int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<GetStructModel>> h = new BeanListHandler(GetStructModel.class);
            String sql = "";

            List<GetStructModel> structs = run.query(sql, h);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext: Exception: " + ex.getMessage());
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
            plugin.getLogger().severe("StructContext:. Exception: " + ex);
            return Optional.empty();
        }
    }
}
