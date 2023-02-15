package org.shrigorevich.ml.admin.handlers;

import com.destroystokyo.paper.entity.ai.Goal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.shrigorevich.ml.admin.NpcAdminService;
import org.shrigorevich.ml.admin.StructAdminService;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.goals.HoldGoal;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.contracts.UserService;

import java.util.Optional;

public class AdminInteractHandler implements Listener {

    private final StructAdminService structAdminService;
    private final StructureService structService;
    private final NpcService npcService;
    private final NpcAdminService npcAdminService;
    private final UserService userService;
    private Villager villager;
    private final Logger logger;

    public AdminInteractHandler(
            StructAdminService structAdminService,
            StructureService structService,
            NpcService npcService,
            NpcAdminService npcAdminService,
            UserService userService) {
        this.structAdminService = structAdminService;
        this.structService = structService;
        this.npcService = npcService;
        this.userService = userService;
        this.npcAdminService = npcAdminService;
        this.logger = LogManager.getLogger("AdminInteractHandler");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Player p = event.getPlayer();

            Optional<User> user = userService.getOnline(p.getName());
            if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN ) return;

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                switch (event.getMaterial()) {
                    case BONE -> draftNpc(event);
                    case FEATHER -> selectStructByLocation(event);
                    case STICK -> draftStructLocation(event);
                    case COAL -> showBlockType(event);
                    case DIAMOND_AXE -> regSafeLocation(event.getClickedBlock().getLocation());
                    case GOLDEN_SWORD -> {
                        Location l1 = event.getClickedBlock().getLocation().add(0, 1, 0);
                        villager = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(npcService.getPlugin(), () -> {
                            Goal<Mob> goal1 = new HoldGoal(npcService.getPlugin(), villager, l1);
                            if (Bukkit.getMobGoals().hasGoal(villager, goal1.getKey())) {
                                Bukkit.getMobGoals().removeGoal(villager, goal1.getKey());
                            }
                            Bukkit.getMobGoals().addGoal(villager, 3, goal1);
                        }, 60);
                    }
                    case GOLDEN_PICKAXE -> {
                        Location l2 = event.getClickedBlock().getLocation().add(0, 1, 0);
                        Goal<Mob> goal2 = new HoldGoal(npcService.getPlugin(), villager, l2);
                        if (Bukkit.getMobGoals().hasGoal(villager, goal2.getKey())) {
                            Bukkit.getMobGoals().removeGoal(villager, goal2.getKey());
                        }
                        Bukkit.getMobGoals().addGoal(villager, 3, goal2);
                    }
                    default -> {
                    }
                }
            } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                switch (event.getMaterial()) {
                    case BONE -> draftNpcSetSpawn(event);
                    case STICK -> {
                        if (event.getClickedBlock() != null) {
                            registerAbodeSpawn(event.getClickedBlock());
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Player p = event.getPlayer();
            Optional<User> user = userService.getOnline(p.getName());
            if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN ) return;

            if (p.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
                if (event.getRightClicked() instanceof Attributable atr) {
                    if (atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                        logger.info("Attack damage: " + atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
                    if (atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                        logger.info("Attack speed: " + atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue());
                    if (atr.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null)
                        logger.info("Max health: " + atr.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    if (atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                        logger.info("Walk speed" + atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
                    logger.info("Health" + ((Mob)event.getRightClicked()).getHealth());
                }
            }
        }
    }

    private void showBlockType(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        Block highest = b.getWorld().getHighestBlockAt(b.getLocation(), HeightMap.MOTION_BLOCKING_NO_LEAVES);
        event.getPlayer().sendMessage(String.format("Highest block: %d %d %d", highest.getX(), highest.getY(), highest.getZ()));
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
            structService.getStruct(event.getClickedBlock().getLocation()).ifPresentOrElse(struct -> {
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
    private void draftStructLocation(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        structAdminService.draftLocation(p.getName(), event.getClickedBlock().getLocation());
        structAdminService.getDraftStruct(p.getName()).ifPresent(struct -> {
            if (struct.getFirstLoc() != null)
                Utils.logLocation(p, struct.getFirstLoc(), "Loc1");
            if (struct.getSecondLoc() != null)
                Utils.logLocation(p, struct.getSecondLoc(), "Loc2");
        });
    }

    private void regSafeLocation(Location l) {
        if (l != null) {
//            structService.getByLocation(l).ifPresent(struct -> {
//                npcService.regSafeLoc(new SafeLocImpl(l.clone().add(0, 1, 0), struct.getId()));
//            });
        }
    }

    private void registerAbodeSpawn(Block block) {
        structService.getStructBlock(block.getLocation())
            .ifPresent(b -> structService.getStruct(b.getStructId())
                .ifPresent(struct -> {
                    if (struct.getType() == StructureType.PRESSURE || struct.getType() == StructureType.ABODE) {
                        structService.registerAbodeSpawn(b);
                        logger.info("Abode spawn registered");
                    }
                }));
    }
}