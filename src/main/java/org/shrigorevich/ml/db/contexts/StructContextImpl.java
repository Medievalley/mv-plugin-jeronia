package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.shrigorevich.ml.domain.structure.models.LoreStructModel;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.structure.models.VolumeModel;
import org.shrigorevich.ml.domain.callbacks.*;
import org.shrigorevich.ml.domain.structure.models.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StructContextImpl implements StructureContext { //TODO: extends abstract context

    private final Plugin plugin;
    private final DataSource dataSource;
    private final BukkitScheduler scheduler;

    public StructContextImpl(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.scheduler = Bukkit.getScheduler();
    }

    public int save(LoreStructDB st) {
        try {

            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            String sql = String.format(
                    "WITH rows as (\n" +
                    "    INSERT INTO struct (type_id, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                    "    VALUES (%d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                    "    RETURNING id\n" +
                    ")\n" +
                    "INSERT INTO lore_struct (struct_id, name) SELECT id, '%s' from rows\n" +
                    "RETURNING struct_id",
                    st.getTypeId(), st.isDestructible(), st.getWorld(),
                    st.getX1(), st.getY1(), st.getZ1(), st.getX2(), st.getY2(), st.getZ2(), st.getName()
            );

            return run.insert(sql, h);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveStructure: " + ex);
            return 0;
        }
    }

    public Optional<LoreStructDB> getById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<LoreStructModel> h = new BeanHandler(LoreStructModel.class);
            String sql = String.format("select ls.struct_id as id, ls.name, ls.volume_id as volumeid, s.type_id as typeid, s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2\n" +
                    "from lore_struct ls JOIN struct s ON s.id = ls.struct_id where ls.struct_id=%d ", id);

            LoreStructModel s = run.query(sql, h);

            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetById: " + ex);
            return Optional.empty();
        }
    }

    public void createVolume(VolumeDB v, List<VolumeBlockDB> volumeBlocks, ISaveVolumeCallback cb) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<Integer> volumeHandler = new ScalarHandler<>();
                String sql = String.format(
                        "INSERT INTO volume (name, size_x, size_y, size_z) VALUES ('%s', %d, %d, %d) returning id;",
                        v.getName(), v.getSizeX(), v.getSizeY(), v.getSizeZ());
                int volumeId = run.query(sql, volumeHandler);

                Object[][] volumeBlockValues = new Object[volumeBlocks.size()][6];

                for (int i = 0; i < volumeBlocks.size(); i++) {
                    VolumeBlockDB b = volumeBlocks.get(i);
                    volumeBlockValues[i] = new Object[] {
                            volumeId,
                            b.getType(),
                            b.getBlockData(),
                            b.getX(),
                            b.getY(),
                            b.getZ()
                        };
                }

                sql = "INSERT INTO volume_block (volume_id, type, block_data, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
                int rows[] = run.batch(sql, volumeBlockValues);
                System.out.println(rows.length);

                scheduler.runTask(plugin, () -> cb.volumeSaved(true, volumeId));

            } catch (SQLException ex) {
                plugin.getLogger().severe("StructContext. SaveStructVolume: "  + ex);
            }
        });
    }

    public List<VolumeBlockDB> getVolumeBlocks(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<VolumeBlockDB>> volumeHandler = new BeanListHandler(VolumeBlockModel.class);
            String sql = String.format("SELECT id, type, block_data as blockdata, x, y, z FROM volume_block where volume_id=%d;", id);
            List<VolumeBlockDB> blocks = run.query(sql, volumeHandler);
            return blocks;
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetVolumeBlocks: " + ex);
            return new ArrayList<>(0);
        }
    }

    public List<LoreStructDB> getStructures() {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<LoreStructDB>> h = new BeanListHandler(LoreStructModel.class);
            String sql = String.format(
                    "select ls.struct_id as id, ls.name, ls.volume_id as volumeid, ls.stock, s.type_id as typeid, \n" +
                    "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2,\n" +
                    "(select count(id)::int from struct_block where struct_id=s.id and broken=true) as brokenBlocks,\n" +
                    "(select count(id)::int from struct_block where struct_id=s.id and broken=false) as blocks\n" +
                    "from lore_struct ls JOIN struct s ON s.id = ls.struct_id");

            List<LoreStructDB> structs = run.query(sql, h);
            for (LoreStructDB s : structs) {
                System.out.printf("Id: %d, TypeId: %d, Name: %s, VolumeId: %d brokenBlocks: %d, Stock: %d%n",
                        s.getId(), s.getTypeId(), s.getName(), s.getVolumeId(), s.getBrokenBlocks(), s.getStock());
            }
            return structs;

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructures: " + ex);
            return new ArrayList<>(0);
        }
    }

    public void setStructVolume(int structId, int volumeId) {
        try {
            System.out.printf("Set volume: %d, %d%n", structId, volumeId);
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE lore_struct SET volume_id = %d where struct_id = %d", volumeId, structId);
            run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SetStructVolume: " + ex);
        }
    }

    public Optional<VolumeDB> getVolumeById(int id) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<VolumeDB> h = new BeanHandler(VolumeModel.class);
            String sql = String.format("SELECT id, size_x as sizex, size_y as sizey, size_z as sizez, name \n" +
                    "FROM volume WHERE id=%d", id);

            VolumeDB volume = run.query(sql, h);
            return volume == null ? Optional.empty() : Optional.of(volume);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetVolumeById: " + ex);
            return Optional.empty();
        }
    }

    public void saveStructBlocks(List<StructBlockDB> blocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] brokenBlockValues = new Object[blocks.size()][3];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockDB b = blocks.get(i);
                brokenBlockValues[i] = new Object[] {
                        b.getStructId(),
                        b.getVolumeBlockId(),
                        b.isTriggerDestruction()
                };
            }
            String sql = "INSERT INTO struct_block (struct_id, volume_block_id, trigger_destruction) VALUES (?, ?, ?)";
            int rows[] = run.batch(sql, brokenBlockValues);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. SaveStructBlocks: " + ex);
        }
    }

    public List<StructBlockDB> getStructBlocks(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<StructBlockDB>> h = new BeanListHandler(StructBlockModel.class);
            String sql = String.format(
                    "select b.id, v.id as volumeBlockId, v.type, v.block_data as blockData, b.broken, b.trigger_destruction as triggerDestruction,\n" +
                    "v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z\n" +
                    "from struct_block b \n" +
                    "join volume_block v ON b.volume_block_id=v.id\n" +
                    "join struct s ON b.struct_id = s.id\n" +
                    "where struct_id=%d", structId);

            return run.query(sql, h);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlocks: " + ex);
            return new ArrayList<>(0);
        }
    }

    //TODO: Maybe create new db index
    public Optional<StructBlockDB> getStructBlock(int x, int y, int z, int volumeId, int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<StructBlockDB> h = new BeanHandler(StructBlockModel.class);
            String sql = String.format(
                    "select s.id, s.struct_id as structId, v.id as volumeBlockId, v.type, v.block_data as blockData, v.x, v.y, v.z, s.broken, s.trigger_destruction as triggerDestruction \n" +
                            "from struct_block s join volume_block v \n" +
                            "ON s.volume_block_id=v.id \n" +
                            "where v.x=%d and v.y=%d and v.z=%d and v.volume_id=%d and struct_id=%d",
                    x, y, z, volumeId, structId);

            StructBlockDB block = run.query(sql, h);
            if (block == null) {
                System.out.println(String.format("Null block: %s, %s, %s, %s, %s", x, y, z, volumeId, structId));
            }

            return block == null ? Optional.empty() : Optional.of(block);

        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
            return Optional.empty();
        }
    }

    public int updateStructBlocksBrokenStatus(List<StructBlockDB> blocks) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            Object[][] blockValues = new Object[blocks.size()][2];

            for (int i = 0; i < blocks.size(); i++) {
                StructBlockDB b = blocks.get(i);
                blockValues[i] = new Object[] {
                        b.isBroken(),
                        b.getId()
                };
            }
            String sql = "UPDATE struct_block SET broken=? WHERE id=?";
            int rows[] = run.batch(sql, blockValues);
            return rows.length;
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. UpdateStructBlocksBrokenStatus: " + ex);
            return 0;
        }
    }

    @Override
    public void delete(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("DELETE FROM struct WHERE id=%d", structId);
            run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
        }
    }

    @Override
    public void restore(int structId) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE struct_block SET broken=false WHERE struct_id=%d and broken=true", structId);
            run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
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
            plugin.getLogger().severe("StructContext. GetStructBlock: " + ex);
        }
    }

    @Override
    public void updateStock(int structId, int stockSize) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            String sql = String.format("UPDATE lore_struct SET stock=%d WHERE struct_id=%d", stockSize, structId);
            run.update(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("StructContext. UpdateStock: " + ex);
        }
    }
}

