package org.shrigorevich.ml.domain.structure;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.common.Coordinates;
import org.shrigorevich.ml.common.CoordinatesImpl;
import org.shrigorevich.ml.domain.callbacks.MsgCallback;
import org.shrigorevich.ml.domain.structure.contracts.*;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final StructureContext context;
    private final Map<Integer, FoodStructure> structures;
    private final Map<String, StructBlockModel> structBlocks;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin, LogManager.getLogger("StructureServiceImpl"));
        this.context = structureContext;
        this.structures = new HashMap<>();
        this.structBlocks = new HashMap<>();
    }

    @Override
    public Optional<Structure> getById (int id) {
        return structures.containsKey(id) ? Optional.of(structures.get(id)) : Optional.empty();
    }

    @Override
    public List<TownInfra> getDamagedStructs() {
        return structures.values().stream().filter(
            s -> structBlocks.values().stream().anyMatch(
                b -> b.getStructId() == s.getId())).collect(Collectors.toList());
    }

    @Override
    public Optional<Structure> getByLocation(Location l) {
        for (FoodStructure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setBlocksBroken(List<StructBlockModel> blocks) {
        context.updateBlocksStatus(blocks, true);
    }

    @Override
    public void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb) {
        try {
            int structId = context.save(
                    name, type.getTypeId(), l1.getWorld().getName(), getMinCoords(l1, l2), getMaxCoords(l1, l2));

            if (structId != 0) {
                Optional<StructModel> model = context.getById(structId);
                model.ifPresent(this::registerStructure);
                cb.result(String.format("StructId: %d", structId));
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            cb.result(e.getMessage());
        }
    }

    @Override
    public void exportVolume(Structure s, String volumeName, MsgCallback cb) {
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
        try {
            int volumeId = context.createVolume(volumeName, s.getSizeX(), s.getSizeY(), s.getSizeZ());
            context.saveVolumeBlocks(volumeId, volumeBlocks);
            cb.result(String.format("VolumeId: %d", volumeId));
        } catch (Exception ex) {
            cb.result(ex.getMessage());
        }
    }

    @Override
    public void restoreBlock(StructBlockModel block) throws Exception {
        try {
            if (structures.containsKey(block.getStructId())) {
                context.restoreBlock(block.getId());
                BlockData bd = Bukkit.createBlockData(block.getBlockData());
                Block b = structures.get(block.getStructId())
                        .getWorld().getBlockAt(block.getX(), block.getY(), block.getZ());
                b.setBlockData(bd);
            } else {
                throw new Exception(String.format("Struct with id %d not found", block.getStructId()));
            }
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    //TODO: this function should restore only broken blocks
    @Override
    public void restore(@NotNull TownInfra struct) {
        try {
            context.restoreStruct(struct.getId()); //TODO: return list of restored blocks (Coords) //TODO: db call
            List<StructBlockModel> blocks = context.getStructBlocks(struct.getId());
            for (StructBlockModel sb : blocks) {
                if (structBlocks.containsKey(getBlockKey(sb))) {
                    structBlocks.get(getBlockKey(sb)).setBroken(false);
                }
                struct.getWorld()
                    .getBlockAt(sb.getX(), sb.getY(), sb.getZ())
                    .setBlockData(Bukkit.createBlockData(sb.getBlockData()));
            }
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void applyVolume(@NotNull TownInfra struct, int volumeId) throws IllegalArgumentException {
        try {
            Optional<VolumeModel> volume = context.getVolumeById(volumeId);
            if (volume.isEmpty()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

            if(!isSizeEqual(struct, volume.get()))
                throw new IllegalArgumentException("Structure and volume sizes are not equal");

            context.detachVolume(struct.getId()); //TODO: db call
            context.attachVolume(struct.getId(), volumeId); //TODO: db call
            List<VolumeBlockModel> volumeBlocks = context.getVolumeBlocks(volumeId);
            List<StructBlockModel> structBlocks = new ArrayList<>();
            for (VolumeBlockModel vb : volumeBlocks) {
                structBlocks.add(new StructBlockModelImpl(struct.getId(), vb.getId(), true));
            }
            context.saveStructBlocks(structBlocks);
            restore(struct);
        } catch (Exception ex) {

        }
        //TODO: need to reload struct
//        struct.volumeId = volumeId;
//        restore(struct);
    }

    //TODO: create fabric
    private void registerStructure(StructModel s) {
        FoodStructure newStruct = new FoodStructImpl(s);
        structures.put(newStruct.getId(), newStruct);
    }

    @Override
    public Optional<StructBlockModel> getBlock(int x, int y, int z) {
        return structBlocks.containsKey(getBlockKey(x, y, z)) ?
                Optional.of(structBlocks.get(getBlockKey(x, y, z))) :
                Optional.empty();
    }

    @Override
    public List<StructBlockModel> getStructBlocks(int structId) {
        return structBlocks.values().stream().filter(s -> s.getId() == structId).collect(Collectors.toList());
    }

    @Override
    public int getBrokenBlocksCount(int structId) throws Exception {
        try {
            return context.getBrokenBlocksCount(structId);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public int getStructBlocksCount(int structId) throws Exception {
        try {
            return context.getBrokenBlocksCount(structId);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void load() throws Exception {
        try {
            List<StructModel> structs = context.getStructures();
            for (StructModel s : structs) {
                registerStructure(s);
            }
            List<StructBlockModel> structBlocks = context.getStructBlocks();
            for (StructBlockModel b : structBlocks) {
                registerBlock(b);
            }
        } catch (Exception ex) {
            throw new Exception("Error while loading structures");
        }
    }

    //not implemented
    public int getStructBlocksCount(int structId, boolean isBroken) {
        return 0;
    }

    private Coordinates getMinCoords(Location l1, Location l2) {
        return new CoordinatesImpl(
            Math.min(l1.getBlockX(), l2.getBlockX()),
            Math.min(l1.getBlockY(), l2.getBlockY()),
            Math.min(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    private Coordinates getMaxCoords(Location l1, Location l2) {
        return new CoordinatesImpl(
            Math.max(l1.getBlockX(), l2.getBlockX()),
            Math.max(l1.getBlockY(), l2.getBlockY()),
            Math.max(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    private boolean isSizeEqual(Structure struct, VolumeModel volume) {
        return struct.getSizeX() == volume.getSizeX() && struct.getSizeY() == volume.getSizeY() &&
            struct.getSizeZ() == volume.getSizeZ();
    }

    private void registerBlock(StructBlockModel b) {
        //TODO: need to implement non collision logic
        structBlocks.put(getBlockKey(b), b);
    }

    private String getBlockKey(int x, int y, int z) {
        return String.format("%d:%d:%d", x, y, z);
    }

    private String getBlockKey(StructBlockModel b) {
        return getBlockKey(b.getX(), b.getY(), b.getZ());
    }



}

