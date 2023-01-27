package org.shrigorevich.ml.admin;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.structure.Structure;

import java.util.*;

public class StructAdminServiceImpl extends BaseService implements StructAdminService {

    private final Map<String, ArrayList<Location>> structCorners;
    private final Map<String, Structure> selectedStruct;


    public StructAdminServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger("StructAdminServiceImpl"));
        this.structCorners = new HashMap<>();
        this.selectedStruct = new HashMap<>();
    }

    @Override
    public void setCorner(String key, Location l) {
        ArrayList<Location> corners = structCorners.computeIfAbsent(key, k -> new ArrayList<>());
        if (corners.size() == 2)
            corners.remove(0);
        corners.add(l);
    }

    @Override
    public Optional<ArrayList<Location>> getStructCorners(String key) {
        ArrayList<Location> locations = structCorners.get(key);
        return locations == null ? Optional.empty() : Optional.of(locations);
    }

    @Override
    public void setSelectedStruct(String key, Structure struct) {
        selectedStruct.put(key, struct);
    }

    @Override
    public Optional<Structure> getSelectedStruct(String key) {
        Structure s = selectedStruct.get(key);
        return s == null ? Optional.empty() : Optional.of(s);
    }

}
