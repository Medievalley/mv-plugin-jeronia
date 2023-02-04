package org.shrigorevich.ml.domain.structure.impl;

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
import org.shrigorevich.ml.domain.structure.*;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModelImpl;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.util.*;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final StructureContext context;
    private final Map<Integer, Structure> structures;
    private final Map<String, ExStructBlock> structBlocks;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin, LogManager.getLogger("StructureServiceImpl"));
        this.context = structureContext;
        this.structures = new HashMap<>();
        this.structBlocks = new HashMap<>();
    }

    @Override
    public Optional<Structure> getStruct(int id) {
        return structures.containsKey(id) ? Optional.of(structures.get(id)) : Optional.empty();
    }

    @Override
    public List<Structure> getStructs(StructureType type) {
        return structures.values().stream().filter(s -> s.getType() == StructureType.REGULAR_ABODE).toList();
    }

    @Override
    public List<Structure> getDamagedStructs() {
        return structures.values().stream().filter(s -> s instanceof TownInfra ti && ti.getBrokenBlocks().size() > 0).toList();
    }

    @Override
    public Optional<Structure> getByLocation(Location l) {
        String key = getBlockKey(l);
        if (structBlocks.containsKey(key) &&
            structures.containsKey(structBlocks.get(key).getStructId())) {
            return Optional.of(structures.get(structBlocks.get(key).getStructId()));
        }
        return Optional.empty();
    }

    @Override
    public void setBlocksBroken(List<StructBlock> blocks) {
        try {
            context.updateBlocksStatus(blocks, true);
            for (StructBlock b : blocks) {
                ((ExStructBlock) b).setIsBroken(true);
            }
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void create(String name, StructureType type, Location l1, Location l2, MsgCallback cb) {
        try {
            int structId = context.save(
                    name, type.getTypeId(), l1.getWorld().getName(), getMinCoords(l1, l2), getMaxCoords(l1, l2));

            if (structId != 0) {
                Optional<StructModel> model = context.getById(structId);
                model.ifPresent(m -> structures.put(m.getId(), createStructure(m, new ArrayList<>())));
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
    public void restoreBlock(StructBlock block) throws Exception {
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

    @Override
    public void restore(int structId) {
        try {
            context.restoreStruct(structId);
            List<StructBlockModel> blocks = context.getStructBlocks(structId);
            context.getById(structId).ifPresent(model -> {
                List<StructBlock> structBlocks = new ArrayList<>();
                for (StructBlockModel b : blocks) {
                    structBlocks.add(new StructBlockImpl(b));
                }

                Structure struct = createStructure(model, structBlocks);
                if (structures.containsKey(structId)) {
                    structures.replace(structId, struct);
                } else {
                    structures.put(structId, struct);
                }
                for (StructBlockModel sb :  blocks) {
                    struct.getWorld()
                        .getBlockAt(sb.getX(), sb.getY(), sb.getZ())
                        .setBlockData(Bukkit.createBlockData(sb.getBlockData()));
                }
            });
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

            context.detachVolume(struct.getId());
            context.attachVolume(struct.getId(), volumeId);
            List<VolumeBlockModel> volumeBlocks = context.getVolumeBlocks(volumeId);
            //TODO: use DraftStructBlock instead of StructBlockModelImpl
            List<StructBlockModel> structBlocks = new ArrayList<>();
            for (VolumeBlockModel vb : volumeBlocks) {
                structBlocks.add(new StructBlockModelImpl(struct.getId(), vb.getId(), true));
            }
            context.saveStructBlocks(structBlocks);
            restore(struct.getId());
        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void updateResources(int structId, int amount) {
        if (structures.containsKey(structId) && structures.get(structId) instanceof ExStorage s) {
            try {
                s.updateResources(amount);
                context.updateResources(structId, s.getResources());
            } catch (Exception ex) {
                getLogger().error(ex.getMessage());
            }
        } else {
            getLogger().error(String.format("Struct %d not found or has no storage", structId));
        }
    }

    private Structure createStructure(StructModel s, List<StructBlock> blocks) {
        StructureType type = StructureType.valueOf(s.getTypeId());
        if (type != null) {
            switch (type) {
                case AGRONOMIC -> {
                    return new FoodStructImpl(s, blocks);
                }
                case MAIN -> {
                    return new MainStructure(s, blocks);
                }
                default -> throw new IllegalArgumentException(
                    String.format("Structure type: %d is not supported", s.getTypeId()));
            }
        } else {
            throw new IllegalArgumentException(String.format("Structure type: %d is not supported", s.getTypeId()));
        }
    }

    @Override
    public Optional<StructBlock> getStructBlock(int x, int y, int z) {
        return structBlocks.containsKey(getBlockKey(x, y, z)) ?
                Optional.of(structBlocks.get(getBlockKey(x, y, z))) :
                Optional.empty();
    }

    @Override
    public Optional<StructBlock> getStructBlock(Location l) {
        return getStructBlock(l.getBlockX(), l.getBlockY(), l.getBlockZ());
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
    public void setup() throws Exception {
        try {
            List<StructModel> structs = context.getStructures();
            HashMap<Integer, List<StructBlock>> blocksPerStruct = new HashMap<>();
            List<StructBlockModel> structBlocks = context.getStructBlocks();

            for (StructBlockModel b : structBlocks) {
                ExStructBlock newBlock = new StructBlockImpl(b);
                if (blocksPerStruct.containsKey(b.getStructId())) {
                    blocksPerStruct.get(b.getStructId()).add(newBlock);
                } else {
                    blocksPerStruct.put(b.getStructId(), List.of(newBlock)); //todo: verify it is correct
                }
                //TODO: need to implement non collision logic
                this.structBlocks.put(getBlockKey(b), newBlock);
            }

            for (StructModel s : structs) {
                structures.put(s.getId(),
                    createStructure(s, blocksPerStruct.getOrDefault(s.getId(), new ArrayList<>())));
            }

        } catch (Exception ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while loading structures");
        }
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

    private String getBlockKey(int x, int y, int z) {
        return String.format("%d:%d:%d", x, y, z);
    }

    private String getBlockKey(StructBlockModel b) {
        return getBlockKey(b.getX(), b.getY(), b.getZ());
    }

    private String getBlockKey(Location l) {
        return getBlockKey(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
}

