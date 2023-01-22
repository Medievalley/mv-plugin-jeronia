package org.shrigorevich.ml.admin;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.common.CoordsImpl;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.Structure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.models.StructModel;
import org.shrigorevich.ml.domain.structure.models.StructModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModelImpl;

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
    public ArrayList<Location> getStructCorners(String key) {
        return structCorners.get(key);
    }

}
