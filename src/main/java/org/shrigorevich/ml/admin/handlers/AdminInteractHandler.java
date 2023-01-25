package org.shrigorevich.ml.admin.handlers;

import com.destroystokyo.paper.entity.ai.Goal;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.shrigorevich.ml.admin.NpcAdminService;
import org.shrigorevich.ml.admin.StructAdminService;
import org.shrigorevich.ml.domain.ai.goals.HoldGoal;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.SafeLocImpl;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.UserServiceImpl;

import java.util.Optional;

public class AdminInteractHandler implements Listener {

    private final StructAdminService structAdminService;
    private final StructureService structService;
    private final NpcService npcService;
    private final NpcAdminService npcAdminService;
    private final UserServiceImpl userService;
    private Villager villager;

    public AdminInteractHandler(
            StructAdminService structAdminService,
            StructureService structService,
            NpcService npcService,
            NpcAdminService npcAdminService,
            UserServiceImpl userService) {
        this.structAdminService = structAdminService;
        this.structService = structService;
        this.npcService = npcService;
        this.userService = userService;
        this.npcAdminService = npcAdminService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player p = event.getPlayer();

        Optional<User> user = userService.getFromOnlineList(p.getName());
        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN ) return;

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            switch (event.getMaterial()) {
                case BONE:
                    draftNpc(event);
                    break;
                case FEATHER:
                    selectStructByLocation(event);
                    break;
                case STICK:
                    setStructCorner(event);
                    break;
                case COAL:
                    showBlockType(event);
                    break;
                case DIAMOND_AXE:
                    regSafeLocation(event.getClickedBlock().getLocation());
                    break;
                case GOLDEN_SWORD:
                    Location l1 = event.getClickedBlock().getLocation().add(0, 1, 0);
                    villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(npcService.getPlugin(), () -> {
                        Goal<Mob> goal1 = new HoldGoal(npcService.getPlugin(), villager, l1);
                        if (Bukkit.getMobGoals().hasGoal(villager, goal1.getKey())) {
                            Bukkit.getMobGoals().removeGoal(villager, goal1.getKey());
                        }
                        Bukkit.getMobGoals().addGoal(villager, 3, goal1);
                    }, 60);
                    break;
                case GOLDEN_PICKAXE:
                    Location l2 = event.getClickedBlock().getLocation().add(0, 1, 0);
                    Goal<Mob> goal2 = new HoldGoal(npcService.getPlugin(), villager, l2);
                    if (Bukkit.getMobGoals().hasGoal(villager, goal2.getKey())) {
                        Bukkit.getMobGoals().removeGoal(villager, goal2.getKey());
                    }
                    Bukkit.getMobGoals().addGoal(villager, 3, goal2);
                    break;
                default:
                    break;
            }
        } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            switch (event.getMaterial()) {
                case BONE -> draftNpcSetSpawn(event);
                default -> {
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEntityEvent event) {

    }

    private void showBlockType(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        Block highest = b.getWorld().getHighestBlockAt(b.getLocation(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
        event.getPlayer().sendMessage(String.format("Highest block: %d %d %d", highest.getX(), highest.getY(), highest.getZ()));

//        if (b.getBlockData() instanceof Ageable) {
//            event.getPlayer().sendMessage(String.format("Age: %s", ((Ageable) b.getBlockData()).getAge()));
//        }
    }

    //TODO: refactor y+1 logic
    private void draftNpc(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Player p = event.getPlayer();
            structService.getStructBlock(event.getClickedBlock().getLocation())
                .ifPresentOrElse(block -> structService.getStruct(block.getStructId())
                    .ifPresentOrElse(struct -> npcAdminService.draftNpcSetWork(
                        block.getX(), block.getY() + 1, block.getZ(),
                        struct.getId(), p.getName(), (p::sendMessage)),
                            () -> event.getPlayer().sendMessage(
                                String.format("Structure with id %d not found", block.getStructId()))),
                        () -> event.getPlayer().sendMessage("Block is not part of any structure"));
        }
    }

    //TODO: refactor y+1 logic
    private void draftNpcSetSpawn(PlayerInteractEvent event) {
        if(event.getClickedBlock() != null) {
            Player p = event.getPlayer();
            Location l = event.getClickedBlock().getLocation();
            npcAdminService.draftNpcSetSpawn(
                l.getBlockX(), l.getBlockY()+1, l.getBlockZ(), p.getName(), (p::sendMessage));
        }
    }

    private void selectStructByLocation(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getClickedBlock() != null) {
            structService.getByLocation(event.getClickedBlock().getLocation()).ifPresentOrElse(struct -> {
                structAdminService.setSelectedStruct(p.getName(), struct);
                p.sendMessage(String.format(
                    "Id: %d\n SizeX: %d\n SizeY: %d\n SizeZ: %d\n",
                    struct.getId(),
                    struct.getX2() - struct.getX1() + 1,
                    struct.getY2() - struct.getY1() + 1,
                    struct.getZ2() - struct.getZ1() + 1));
            }, () -> p.sendMessage("This location is not part of any structure"));
        };
    }
    private void setStructCorner(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        structAdminService.setCorner(p.getName(), event.getClickedBlock().getLocation());
        structAdminService.getStructCorners(p.getName()).ifPresent(locs -> {
            for(Location l : locs) {
                p.sendMessage(String.format("%d, %d, %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
            }
        });
    }
    private void regSafeLocation(Location l) {
        if (l != null) {
            structService.getByLocation(l).ifPresent(struct -> {
                npcService.regSafeLoc(new SafeLocImpl(l.clone().add(0, 1, 0), struct.getId()));
            });
        }
    }
}