package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.project.models.StorageModel;
import org.shrigorevich.ml.domain.project.models.StorageModelImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ProjectContextImpl extends Context implements ProjectContext {
    public ProjectContextImpl(Plugin plugin, DataSource dataSource) {
        super(plugin, dataSource);
    }

    @Override
    public StorageModel getStorage() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StorageModel> h = new BeanHandler(StorageModelImpl.class);
            String sql = "SELECT deposit, resources FROM storage ORDER BY id asc limit 1";

            return run.query(sql, h);
        } catch (SQLException ex) {
            getPlugin().getLogger().severe("ProjectContext. Get storage: " + ex);
            return new StorageModelImpl();
        }
    }
}
