package org.shrigorevich.ml.admin.handlers;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import com.destroystokyo.paper.event.entity.EntityZapEvent;
import com.destroystokyo.paper.event.profile.LookupProfileEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShowEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.shrigorevich.ml.admin.NpcAdminService;
import org.shrigorevich.ml.admin.StructAdminService;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.ai.goals.legacy.HoldGoal;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.contracts.UserService;

import java.util.Optional;

public class TestHandler implements Listener {

    private final StructAdminService structAdminService;
    private final StructureService structService;
    private final NpcService npcService;
    private final NpcAdminService npcAdminService;
    private final UserService userService;
    private Villager villager;
    private final Logger logger;

    public TestHandler(
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
        this.logger = LogManager.getLogger("TestHandler");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteract(EntityTargetEvent event) {

    }
}