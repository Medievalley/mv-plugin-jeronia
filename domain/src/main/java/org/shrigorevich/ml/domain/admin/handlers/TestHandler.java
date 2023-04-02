package org.shrigorevich.ml.domain.admin.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.shrigorevich.ml.domain.admin.NpcAdminService;
import org.shrigorevich.ml.domain.admin.StructAdminService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.structures.StructureService;
import org.shrigorevich.ml.domain.users.UserService;

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
}