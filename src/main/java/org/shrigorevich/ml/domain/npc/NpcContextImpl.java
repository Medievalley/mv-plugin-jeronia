package org.shrigorevich.ml.domain.npc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.Context;
import org.shrigorevich.ml.domain.npc.contracts.NpcContext;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class NpcContextImpl extends Context implements NpcContext {


    public NpcContextImpl(DataSource dataSource) {
        super(dataSource, Logger.getLogger("NpcContextImpl"));
    };

    @Override
    public int save(StructNpcDB npc) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            String workLocSql = String.format(
                    "INSERT INTO location (x, y, z) VALUES (%d, %d, %d) returning id",
                    npc.getWorkX(), npc.getWorkY(), npc.getWorkZ());
            String spawnLocSql = String.format(
                    "INSERT INTO location (x, y, z) VALUES (%d, %d, %d) returning id",
                    npc.getSpawnX(), npc.getSpawnY(), npc.getSpawnZ());

            int workLocId = run.insert(workLocSql, h);
            int spawnLocId = run.insert(spawnLocSql, h);

            String sql = String.format(
                    "INSERT INTO struct_npc (name, role_id, struct_id, spawn, work) VALUES ('%s', %d, %d, %d, %d) returning id",
                    npc.getName(), npc.getRoleId(), npc.getStructId(), spawnLocId, workLocId);
            return run.insert(sql, h);

        } catch (SQLException ex) {
            getLogger().severe("NpcContext. Save: " + ex);
            return 0;
        }
    }

    @Override
    public List<StructNpcDB> get() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcDB>> h = new BeanListHandler(StructNpcModel.class);
            String sql = "select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,\n" +
            "sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ\n" +
            "from struct_npc n \n" +
            "join struct s on s.id = n.struct_id\n" +
            "join location sl on sl.id = n.spawn\n" +
            "join location wl on wl.id = n.work";
            return run.query(sql, h);
        } catch (SQLException ex) {
            getLogger().severe("NpcContext. Get all: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<StructNpcDB> getByStructId(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcDB>> h = new BeanListHandler(StructNpcModel.class);
            String sql = String.format(
                    "select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,\n" +
                    "sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ\n" +
                    "from struct_npc n \n" +
                    "join struct s on s.id = n.struct_id\n" +
                    "join location sl on sl.id = n.spawn\n" +
                    "join location wl on wl.id = n.work\n" +
                    "where n.struct_id = %d", structId);

            return run.query(sql, h);
        } catch (SQLException ex) {
            getLogger().severe("NpcContext. Get by structId: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public Optional<StructNpcDB> get(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructNpcDB> h = new BeanHandler(StructNpcModel.class);
            String sql = String.format(
                    "select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,\n" +
                    "sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ\n" +
                    "from struct_npc n \n" +
                    "join struct s on s.id = n.struct_id\n" +
                    "join location sl on sl.id = n.spawn\n" +
                    "join location wl on wl.id = n.work\n" +
                    "where n.id = %d", id);

            StructNpcDB s = run.query(sql, h);
            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            getLogger().severe("NpcContext. GetById: " + ex);
            return Optional.empty();
        }
    }
}
