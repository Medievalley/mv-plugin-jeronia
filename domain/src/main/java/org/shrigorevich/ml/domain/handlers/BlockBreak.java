package org.shrigorevich.ml.domain.handlers;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.shrigorevich.ml.domain.events.StructsDamagedEvent;
import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.domain.structures.StructureService;

import java.util.*;

public class BlockBreak implements Listener {
    StructureService structSvc;
    public BlockBreak(StructureService structureService) {
        this.structSvc = structureService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockBreakEvent event) {
        processDestroyedBlocksAsync(new ArrayList<>(List.of(event.getBlock())));
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBroken(BlockDestroyEvent event) {
        processDestroyedBlocksAsync(new ArrayList<>(List.of(event.getBlock())));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockExploded(EntityExplodeEvent event) {
        processDestroyedBlocksAsync(event.blockList());
    }

    //TODO: need to overload
    private void processDestroyedBlocksAsync(List<Block> blocks) {
        List<StructBlock> brokenBlocks = new ArrayList<>();
        for (Block block : blocks) {
            getBrokenBlock(block).ifPresent(brokenBlocks::add);
        }
        structSvc.setBlocksBroken(brokenBlocks);

        Map<Integer, List<StructBlock>> blocksPerStruct = new HashMap<>();
        for (StructBlock b : brokenBlocks) {
            if(blocksPerStruct.containsKey(b.getStructId())) {
                blocksPerStruct.get(b.getStructId()).add(b);
            } else {
                blocksPerStruct.put(b.getStructId(), new ArrayList<>(Collections.singletonList(b)));
            }
        }
        //TODO: move call event logic to handler
        if (!blocksPerStruct.isEmpty()) {
            Bukkit.getScheduler().runTask(structSvc.getPlugin(),
                    () -> Bukkit.getPluginManager().callEvent(new StructsDamagedEvent(blocksPerStruct)));
        }
    }

    private Optional<StructBlock> getBrokenBlock(Block block) {
        Optional<StructBlock> sb = structSvc.getStructBlock(block.getLocation());
        if (sb.isPresent() && !sb.get().isBroken() && sb.get().isHealthPoint()) {
            return sb;
        }
        return Optional.empty();
//            int x = block.getX() - optionalStruct.get().getX1();
//            int y = block.getY() - optionalStruct.get().getY1();
//            int z = block.getZ() - optionalStruct.get().getZ1();
    }
}
