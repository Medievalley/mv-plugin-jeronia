package org.shrigorevich.ml.domain.admin.handlers;

import com.destroystokyo.paper.entity.ai.Goal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
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
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.admin.NpcAdminService;
import org.shrigorevich.ml.domain.admin.StructAdminService;
import org.shrigorevich.ml.domain.ai.goals.GoGoal;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structures.StructureService;
import org.shrigorevich.ml.domain.structures.StructureType;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.UserService;

import java.util.Optional;

public class AdminInteractHandler implements Listener {

    private final StructAdminService structAdminService;
    private final StructureService structService;
    private final NpcService npcService;
    private final NpcAdminService npcAdminService;
    private final UserService userService;
    private Villager villager;
    private final Logger logger;
    private Mob customMob;
    private final Plugin plugin;

    public AdminInteractHandler(
            StructAdminService structAdminService,
            StructureService structService,
            NpcService npcService,
            NpcAdminService npcAdminService,
            UserService userService, Plugin plugin) {
        this.structAdminService = structAdminService;
        this.structService = structService;
        this.npcService = npcService;
        this.userService = userService;
        this.npcAdminService = npcAdminService;
        this.plugin = plugin;
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
                    case FEATHER -> showStructInfo(event);
                    case STICK -> draftStructLocation(event);
                    case DIAMOND_AXE -> regSafeLocation(event.getClickedBlock().getLocation());
                    case COAL -> {

                        event.getPlayer().sendMessage("Y+1: " + Bukkit.getWorld("world")
                            .getBlockAt(event.getClickedBlock().getLocation().clone().add(0,1,0))
                            .getLightFromBlocks());
                    }
                    case GOLDEN_PICKAXE -> {
                        setMobAI(event.getClickedBlock().getLocation().add(0, 1, 0));
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

            switch (p.getEquipment().getItemInMainHand().getType()) {
                case DIAMOND_SWORD -> this.customMob = (Mob) event.getRightClicked();
                case GOLDEN_SWORD -> {
                    event.getRightClicked().addPassenger(customMob);
                }
            }
        }
    }

    private void setMobAI(Location location) {
        if (customMob != null) {
            Goal<Mob> goal2 = new GoGoal(customMob, location, plugin);
            Bukkit.getMobGoals().removeAllGoals(customMob);
            Bukkit.getMobGoals().addGoal(customMob, 1, goal2);
        }
    }

    private void checkAttrs(Entity entity) {
        if (entity instanceof Attributable atr) {
            if (atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                logger.info("Attack damage: " + atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
            if (atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                logger.info("Attack speed: " + atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue());
            if (atr.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null)
                logger.info("Max health: " + atr.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            if (atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                logger.info("Walk speed" + atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
            logger.info("Health" + ((Mob)entity).getHealth());
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

    private void showStructInfo(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getClickedBlock() != null) {
            structService.getStruct(event.getClickedBlock().getLocation()).ifPresentOrElse(struct -> p.sendMessage(String.format(
                "Id: %d\n SizeX: %d\n SizeY: %d\n SizeZ: %d\n",
                struct.getId(),
                struct.getX2() - struct.getX1() + 1,
                struct.getY2() - struct.getY1() + 1,
                struct.getZ2() - struct.getZ1() + 1)), () -> p.sendMessage("This location is not part of any structure"));
        };
    }
    private void draftStructLocation(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        structAdminService.draftLocation(p.getName(), event.getClickedBlock().getLocation());
        structAdminService.getDraftStruct(p.getName()).ifPresent(struct -> {
            if (struct.getFirstLoc() != null)
                logLocation(p, struct.getFirstLoc(), "Loc1");
            if (struct.getSecondLoc() != null)
                logLocation(p, struct.getSecondLoc(), "Loc2");
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

    private void logLocation(Player player, Location l, String key) {
        player.sendMessage(String.format("%s: %d, %d, %d", key, l.getBlockX(), l.getBlockY(), l.getBlockZ()));
    }
}