package org.shrigorevich.ml.listeners;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.shrigorevich.ml.domain.structure.contracts.FoodStructure;
import org.shrigorevich.ml.domain.structure.contracts.Structure;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.events.StructsDamagedEvent;

import java.util.*;

public class BlockBreak implements Listener {
    StructureService structSvc;
    public BlockBreak(StructureService structureService) {
        this.structSvc = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockBreakEvent event) {
        processDestroyedBlocksAsync(new ArrayList<>(Collections.singletonList(event.getBlock())));
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockDestroyEvent event) {
        processDestroyedBlocksAsync(new ArrayList<>(Collections.singletonList(event.getBlock())));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(EntityExplodeEvent event) {
        processDestroyedBlocksAsync(event.blockList());
    }

    private void processDestroyedBlocksAsync(List<Block> blocks) {
        List<StructBlockModel> brokenBlocks = new ArrayList<>();
        for (Block block : blocks) {
            Optional<StructBlockModel> b = getBrokenBlock(block);
            b.ifPresent(brokenBlocks::add);
        }
        structSvc.setBlocksBroken(brokenBlocks);

        Map<Integer, List<StructBlockModel>> blocksPerStruct = new HashMap<>();
        for (StructBlockModel b : brokenBlocks) {
            if(blocksPerStruct.containsKey(b.getStructId())) {
                blocksPerStruct.get(b.getStructId()).add(b);
            } else {
                blocksPerStruct.put(b.getStructId(), new ArrayList<>(Collections.singletonList(b)));
            }
        }
        if (!blocksPerStruct.isEmpty()) {
            Bukkit.getScheduler().runTask(structSvc.getPlugin(),
                    () -> Bukkit.getPluginManager().callEvent(new StructsDamagedEvent(blocksPerStruct)));
        }
    }

    //TODO: refactor struct logic
    private Optional<StructBlockModel> getBrokenBlock(Block block) {
        Optional<Structure> optional = structSvc.getByLocation(block.getLocation());
        if (optional.isPresent()) {
            FoodStructure struct = (FoodStructure) optional.get();
            int x = block.getX() - struct.getX1();
            int y = block.getY() - struct.getY1();
            int z = block.getZ() - struct.getZ1();
            Optional<StructBlockModel> sb = struct.getBlock(x, y, z);

            if (sb.isPresent() && !sb.get().isBroken() && sb.get().isTriggerDestruction()) {
                sb.get().setBroken(true);
                return sb;
            }
        }
        return Optional.empty();
    }

//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void DoorBrokenByEntity(EntitySpellCastEvent event) {
//        //TODO: Check to possibility of creating custom spells
//    }
}
