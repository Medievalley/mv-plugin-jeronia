package org.shrigorevich.ml.domain.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.shrigorevich.ml.domain.structures.DraftStruct;
import org.shrigorevich.ml.domain.structures.Structure;
import org.shrigorevich.ml.domain.structures.StructureType;

import java.util.*;

public class StructAdminServiceImpl implements StructAdminService {

    private final Map<String, DraftStruct> draftStructs;
    private final Map<String, Structure> selectedStruct;
    private final Logger logger;


    public StructAdminServiceImpl() {
        this.logger = LogManager.getLogger("StructAdminServiceImpl");
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
