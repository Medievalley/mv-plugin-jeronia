package org.shrigorevich.ml.domain.village;

import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.db.contexts.VillageContext;
import org.shrigorevich.ml.domain.BaseService;

public class VillageServiceImpl extends BaseService implements VillageService {

    private final VillageContext context;
    public VillageServiceImpl(Plugin plugin, VillageContext context) {
        super(plugin);
        this.context = context;
    }

    @Override
    public void load() {
    }
}
