package org.shrigorevich.ml.domain.npc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.npc.contracts.NpcContext;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;
import org.shrigorevich.ml.domain.npc.models.StructNpcModelImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NpcContextImpl extends BaseContext implements NpcContext {

    private final NpcQueryBuilder queryBuilder;

    public NpcContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("NpcContextImpl"));
        this.queryBuilder = new NpcQueryBuilder();
    };

    @Override
    public int save(StructNpcModel npc) {
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
            getLogger().error("NpcContext. Save: " + ex);
            return 0;
        }
    }

    @Override
    public List<StructNpcModel> get() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcModel>> h = new BeanListHandler(StructNpcModelImpl.class);
            String sql = "select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,\n" +
            "sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ\n" +
            "from struct_npc n \n" +
            "join struct s on s.id = n.struct_id\n" +
            "join location sl on sl.id = n.spawn\n" +
            "join location wl on wl.id = n.work";
            return run.query(sql, h);
        } catch (SQLException ex) {
            getLogger().error("NpcContext. Get all: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<StructNpcModel> getByStructId(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcModel>> h = new BeanListHandler(StructNpcModelImpl.class);
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
            getLogger().error("NpcContext. Get by structId: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public Optional<StructNpcModel> get(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructNpcModel> h = new BeanHandler(StructNpcModelImpl.class);
            String sql = String.format(
                    "select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,\n" +
                    "sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ\n" +
                    "from struct_npc n \n" +
                    "join struct s on s.id = n.struct_id\n" +
                    "join location sl on sl.id = n.spawn\n" +
                    "join location wl on wl.id = n.work\n" +
                    "where n.id = %d", id);

            StructNpcModel s = run.query(sql, h);
            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            getLogger().error("NpcContext. GetById: " + ex);
            return Optional.empty();
        }
    }
}
