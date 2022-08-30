package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.BaseService;
import org.shrigorevich.ml.domain.structure.models.LoreStructModel;
import org.shrigorevich.ml.domain.structure.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.structure.models.VolumeModel;
import org.shrigorevich.ml.domain.callbacks.IResultCallback;
import org.shrigorevich.ml.db.contexts.StructureContext;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.events.ProjectUpdatedEvent;
import java.util.*;

public class StructureServiceImpl extends BaseService implements StructureService {
    private final Map<String, ArrayList<Location>> structCorners;
    private final StructureContext structContext;
    private final Map<Integer, LoreStructure> structures;
    private final Map<String, Structure> selectedStruct;
    private final PriorityQueue<LoreStructure> buildPlan;
    private LoreStructure currentProject;

    public StructureServiceImpl(StructureContext structureContext, Plugin plugin) {
        super(plugin);
        this.structContext = structureContext;
        this.structCorners = new HashMap<>();
        this.structures = new HashMap<>();
        this.selectedStruct = new HashMap<>();
        this.buildPlan = new PriorityQueue<>();
    }

    @Override
    public void create(
            User user, String type,
            String name, boolean destructible,
            IResultCallback cb
    ) throws Exception  {
        LoreStructModel m = new LoreStructModel();
        ArrayList<Location> corners = structCorners.get(user.getName());
        if (corners == null || corners.size() != 2) {
            throw new Exception("Structure location not set");
        }
        applyLocation(m, corners.get(0), corners.get(1));
        m.typeId = StructureType.valueOf(type.toUpperCase()).getTypeId();
        m.destructible = destructible;
        m.name = name;
        m.world = corners.get(0).getWorld().getName();

        int structId = structContext.save(m);
        if (structId != 0) {
            Optional<LoreStructDB> model = structContext.getById(structId);
            model.ifPresent(loreStructDB -> registerStructure(new LoreStructImpl(loreStructDB, structContext)));
            cb.sendResult(true, String.format("StructId: %d", structId));
        }
    }

    private void applyLocation(LoreStructModel m, Location l1, Location l2) {
        m.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        m.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        m.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        m.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        m.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        m.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    }

    public void setCorner(String key, Location l) {
        ArrayList<Location> corners = structCorners.computeIfAbsent(key, k -> new ArrayList<>());
        if (corners.size() == 2)
            corners.remove(0);
        corners.add(l);
    }

    public ArrayList<Location> getStructCorners(String key) {
        return structCorners.get(key);
    }

    private void registerStructure(LoreStructure s) {
        structures.put(s.getId(), s);
        if (s.getDestructionPercent() > 0) {
            buildPlan.add(s);
        }
    }
    public Optional<LoreStructure> getById (int id) {
        LoreStructure struct = structures.get(id);
        return struct != null ? Optional.of(struct) : Optional.empty();
    }
    @Override
    public Optional<LoreStructure> getByLocation(Location l) {
        for (LoreStructure s : structures.values()) {
            if(s.contains(l)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
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
    public void exportVolume(String userName, String volumeName, IResultCallback cb) {
        Structure s = selectedStruct.get(userName);
        if (s != null) {
            VolumeModel v = new VolumeModel(
                    volumeName,
                    s.getSizeX(),
                    s.getSizeY(),
                    s.getSizeZ()
            );

            List<VolumeBlockDB> volumeBlocks = new ArrayList<>();
            List<Block> blocks = s.getBlocks();
            int offsetX = blocks.get(0).getX();
            int offsetY = blocks.get(0).getY();
            int offsetZ = blocks.get(0).getZ();

            for (Block b : blocks) {
                if (!b.getType().isAir()) {
                    volumeBlocks.add(
                            new VolumeBlockModel(
                                    b.getX() - offsetX,
                                    b.getY() - offsetY,
                                    b.getZ() - offsetZ,
                                    b.getType().toString(),
                                    b.getBlockData().getAsString()
                            ));
                }
            }
            structContext.createVolume(v, volumeBlocks,
                    (res, volumeId) -> cb.sendResult(res, String.format("VolumeId: %d", volumeId)));
        } else {
            cb.sendResult(false, "Please choose struct by right click to any struct block");
        }
    }

    @Override
    public void load() {
        List<LoreStructDB> structs = structContext.getStructures();
        for (LoreStructDB s : structs) {
            LoreStructure newStruct = new LoreStructImpl(s, structContext);
            registerStructure(newStruct);
        }
        if (!buildPlan.isEmpty()) {
            System.out.printf("Plan size: %d%n", buildPlan.size());
            currentProject = buildPlan.poll();
            Bukkit.getScheduler().runTask(getPlugin(),
                () -> Bukkit.getPluginManager().callEvent(new ProjectUpdatedEvent(currentProject)));
        }
    }

    @Override
    public void processExplodedBlocksAsync(List<Block> blocks) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            List<StructBlockDB> brokenBlocks = new ArrayList<>();
            for (Block block : blocks) {
                Optional<StructBlockDB> b = getBrokenBlock(block);
                b.ifPresent(brokenBlocks::add);
            }
            structContext.updateStructBlocksBrokenStatus(brokenBlocks);

            List<LoreStructure> damagedStructures = new ArrayList<>();
            for (StructBlockDB b : brokenBlocks) {
                boolean exists = damagedStructures.stream().anyMatch(s -> s.getId() == b.getStructId());
                if(!exists && getById(b.getStructId()).isPresent()) {
                    damagedStructures.add(getById(b.getStructId()).get());
                }
            }
            updateBuildPlan(damagedStructures);
        });
    }

    @Override
    public Optional<LoreStructure> getProject() {
        return currentProject == null ? Optional.empty() : Optional.of(currentProject);
    }

    private void postponeProject() {
        buildPlan.add(currentProject);
        currentProject = null;
    }

    private void updateBuildPlan(List<LoreStructure> damagedStructs) {
        if (!damagedStructs.isEmpty()) {
            boolean isProjectExists = currentProject != null;
            for (LoreStructure ls : damagedStructs) {
                boolean alreadyInPlan = buildPlan.stream().noneMatch(s -> s.getId() == ls.getId());
                boolean matchCurrent = isProjectExists && currentProject.getId() != ls.getId();
                if (alreadyInPlan && matchCurrent) {
                    buildPlan.add(ls);
                }
            }
            if (!isProjectExists) {
                currentProject = buildPlan.poll();
            } else if (buildPlan.peek().getPriority() > currentProject.getPriority()){
                postponeProject();
                currentProject = buildPlan.poll();
            }
            if (!buildPlan.isEmpty()) {
                System.out.printf("Plan size after updating: %d. Id: %d%n", buildPlan.size(), buildPlan.peek().getId());
            }
            if (damagedStructs.stream().anyMatch(s -> s.getId() == currentProject.getId())) {
                Bukkit.getScheduler().runTask(getPlugin(),
                        () -> Bukkit.getPluginManager().callEvent(new ProjectUpdatedEvent(currentProject)));
            }
        }
    }
    private Optional<StructBlockDB> getBrokenBlock(Block block) {
        Optional<LoreStructure> struct = getByLocation(block.getLocation());
        if (struct.isPresent()) {
            LoreStructure s = struct.get();
            int x = block.getX() - s.getX1();
            int y = block.getY() - s.getY1();
            int z = block.getZ() - s.getZ1();
            Optional<StructBlockDB> sb = structContext.getStructBlock(x, y, z, s.getVolumeId(), s.getId());

            if (sb.isPresent() && !sb.get().isBroken() && sb.get().isTriggerDestruction()) {
                sb.get().setBroken(true);
                return sb;
            }
        }
        return Optional.empty();
    }
}

