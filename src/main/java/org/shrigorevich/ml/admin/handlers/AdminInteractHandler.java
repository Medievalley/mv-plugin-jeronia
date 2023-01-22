package org.shrigorevich.ml.admin;

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
import org.shrigorevich.ml.domain.ai.goals.HoldGoal;
import org.shrigorevich.ml.domain.npc.contracts.NpcService;
import org.shrigorevich.ml.domain.npc.SafeLocImpl;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.UserServiceImpl;

import java.util.Optional;

public class AdminInteractHandler implements Listener {

    private final StructureService structureService;
    private final NpcService npcService;
    private final UserServiceImpl userService;
    private Villager villager;

    public AdminInteractHandler(StructureService structureService, NpcService npcService, UserServiceImpl userService) {
        this.structureService = structureService;
        this.npcService = npcService;
        this.userService = userService;
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
                case BONE:
                    draftNpcSetSpawn(event);
                    break;
                default:
                    break;
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
    private void draftNpc(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Player p = event.getPlayer();
            Location l = event.getClickedBlock().getLocation();

            Optional<LoreStructure> s = structureService.getByLocation(l);

            s.ifPresent(structure -> npcService.draftNpc(
                    l.getBlockX(), l.getBlockY() + 1, l.getBlockZ(),
                    structure.getId(), p.getName(), (p::sendMessage)));
        }
    }
    private void draftNpcSetSpawn(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Location l = event.getClickedBlock().getLocation().clone().add(0, 1, 0);
        npcService.draftNpcSetSpawn(l.getBlockX(), l.getBlockY(), l.getBlockZ(), p.getName(), (p::sendMessage));
    }
    private void selectStructByLocation(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        structureService.selectStructByLocation(p.getName(), event.getClickedBlock().getLocation(), ((result, msg) -> {
            p.sendMessage(msg);
        }));
    }
    private void setStructCorner(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        structureService.setCorner(p.getName(), event.getClickedBlock().getLocation());
        for(Location l : structureService.getStructCorners(p.getName())) {
            p.sendMessage(String.format("%d, %d, %d", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }
    private void regSafeLocation(Location l) {
        if (l != null) {
            structureService.getByLocation(l).ifPresent(struct -> {
                npcService.regSafeLoc(new SafeLocImpl(l.clone().add(0, 1, 0), struct.getId()));
            });
        }
    }
}