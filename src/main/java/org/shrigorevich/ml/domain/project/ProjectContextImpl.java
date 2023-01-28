package org.shrigorevich.ml.domain.project;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.project.models.StorageModel;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ProjectContextImpl extends BaseContext implements ProjectContext {
    public ProjectContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("ProjectContextImpl"));
    }

    @Override
    public StorageModel getStorage(int storageId) {
        return null;
    }

    @Override
    public void updateResources(int structId, int amount) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(String.format("UPDATE struct SET resources=%d WHERE id=%d", amount, structId));
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
        }
    }
}
