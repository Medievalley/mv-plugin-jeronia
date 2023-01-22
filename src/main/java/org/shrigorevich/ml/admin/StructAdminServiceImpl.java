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


    public StructAdminServiceImpl(Plugin plugin, StructureContext ctx) {
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

    @Override
    public void create(String name, StructureType type, Coords l1, Coords l2) {

        StructModelImpl m = new StructModelImpl();
        applyLocation(m, corners.get(0), corners.get(1));
        m.typeId = StructureType.valueOf(type.toUpperCase()).getTypeId();
        m.name = name;
        m.world = corners.get(0).getWorld().getName();

        int structId = structContext.save(m);
        if (structId != 0) {
            Optional<StructModel> model = structContext.getById(structId);
            model.ifPresent(this::registerStructure);
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    @Override
    public void selectStructByLocation(String userName, Location l, IResultCallback cb) {
        Optional<LoreStructure> struct = this.getByLocation(l);
        if (struct.isPresent()) {
            Structure s = struct.get();
            selectedStruct.put(userName, s);
            cb.sendResult(true, String.format(
                    "Id: %d\n SizeX: %d\n SizeY: %d\n SizeZ: %d\n",
                    s.getId(),
                    s.getX2() - s.getX1() + 1,
                    s.getY2() - s.getY1() + 1,
                    s.getZ2() - s.getZ1() + 1)
            );
        } else {
            cb.sendResult(false, "This location is not part of any structure");
        }
    }

    @Override
    public String exportVolume(String userName, String volumeName) {
        Structure s = selectedStruct.get(userName);
        if (s != null) {
            VolumeModelImpl v = new VolumeModelImpl(
                    volumeName,
                    s.getSizeX(),
                    s.getSizeY(),
                    s.getSizeZ()
            );

            List<VolumeBlockModel> volumeBlocks = new ArrayList<>();
            List<Block> blocks = s.getBlocks();
            int offsetX = blocks.get(0).getX();
            int offsetY = blocks.get(0).getY();
            int offsetZ = blocks.get(0).getZ();

            for (Block b : blocks) {
                if (!b.getType().isAir()) {
                    volumeBlocks.add(
                            new VolumeBlockModelImpl(
                                    b.getX() - offsetX,
                                    b.getY() - offsetY,
                                    b.getZ() - offsetZ,
                                    b.getType().toString(),
                                    b.getBlockData().getAsString()
                            ));
                }
            }
            int volumeId = structContext.createVolume(v, volumeBlocks);
            return String.format("VolumeId: %d", volumeId);
        } else {
            return "Error occurred";
        }
    }

    //TODO: move to right place
    public Coords getMinCoords(Location l1, Location l2) {
        return new CoordsImpl(
            Math.min(l1.getBlockX(), l2.getBlockX()),
            Math.min(l1.getBlockY(), l2.getBlockY()),
            Math.min(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    public Coords getMaxCoords(Location l1, Location l2) {
        return new CoordsImpl(
            Math.max(l1.getBlockX(), l2.getBlockX()),
            Math.max(l1.getBlockY(), l2.getBlockY()),
            Math.max(l1.getBlockZ(), l2.getBlockZ())
        );
    }



}