package org.shrigorevich.ml.admin;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.structure.DraftStruct;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.domain.structure.StructureType;

import java.util.*;

public class StructAdminServiceImpl extends BaseService implements StructAdminService {

    private final Map<String, DraftStruct> draftStructs;
    private final Map<String, Structure> selectedStruct;


    public StructAdminServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger("StructAdminServiceImpl"));
        this.draftStructs = new HashMap<>();
        this.selectedStruct = new HashMap<>();
    }

    @Override
    public void draftLocation(String key, Location l) {
        draftStructs.computeIfAbsent(key, k -> new DraftStructImpl()).addLocation(l);
    }

    @Override
    public Optional<DraftStruct> getDraftStruct(String key) {
        return draftStructs.containsKey(key) ? Optional.of(draftStructs.get(key)) : Optional.empty();
    }

    @Override
    public void draftName(String key, String name) {
        draftStructs.computeIfAbsent(key, k -> new DraftStructImpl()).name(name);
    }

    @Override
    public void draftType(String key, StructureType type) {
        draftStructs.computeIfAbsent(key, k -> new DraftStructImpl()).type(type);
    }

    @Override
    public boolean isStructValid(DraftStruct struct) {
        return struct.isLocated() && struct.type() != null;
    }

}
