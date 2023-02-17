package org.shrigorevich.ml.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.admin.StructAdminService;
import org.shrigorevich.ml.common.Utils;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.TownInfra;
import org.shrigorevich.ml.domain.structure.VolumeStruct;
import org.shrigorevich.ml.domain.users.UserRole;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.users.contracts.UserService;
import org.shrigorevich.ml.domain.project.events.FinalizeProjectEvent;

import java.util.Optional;

public class StructureExecutor implements CommandExecutor {
    private final UserService userService;
    private final StructureService structService;
    private final ProjectService projectService;
    private final StructAdminService structAdminService;
    private final Logger logger;

    public StructureExecutor(
            UserService userService,
            StructureService structService,
            ProjectService projectService,
            StructAdminService structAdminService) {
        this.userService = userService;
        this.structService = structService;
        this.projectService = projectService;
        this.structAdminService = structAdminService;
        this.logger = LogManager.getLogger("StructureExecutor");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0) {
            if(sender instanceof Player player){
                try {
                    Optional<User> user = userService.getOnline(player.getName());
                    if (user.isPresent() && user.get().getRole().accessLevel() < UserRole.MODER.accessLevel())
                        throw new Exception("You do not have permission for this operation");

                    switch (args[0].toLowerCase()) {
                        case "restore", "r" -> restore(args[1]);
                        case "c", "create" -> create(player, args[1], args[2]);
                        case "sv", "save_volume" -> exportVolume(player, Integer.parseInt(args[1]), args[2]);
                        case "av", "apply_volume" -> applyVolume(player, args[1], args[2]);
                        default ->
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                    }
                } catch (Exception ex) {
                    logger.error(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                logger.error("You can`t use this command through console");
            }
        }
        return true;
    }

    private void applyVolume(Player player, String structId, String volumeId) {
        structService.getStruct(Integer.parseInt(structId)).ifPresent(s -> {
            try {
                if(s instanceof VolumeStruct vs) {
                    projectService.finalizeProject(vs.getId());
                    structService.applyVolume(vs, Integer.parseInt(volumeId));
                } else {
                    player.sendMessage(ChatColor.RED + "Not suitable structure");
                }
            } catch (Exception ex) {
                player.sendMessage(ChatColor.RED + ex.getMessage());
            }
        });
    }

    private void restore(String structId) {
        structService.getStruct(Integer.parseInt(structId)).ifPresent(s -> {
            if (s instanceof TownInfra) {
                structService.restore(s.getId());
                projectService.getProject(s.getId()).ifPresent(project ->
                    Bukkit.getServer().getPluginManager().callEvent(new FinalizeProjectEvent(project)));
            }
        });
    }

    private void create(Player player, String name, String type) {
        try {
            structAdminService.draftName(player.getName(), name);
            structAdminService.draftType(player.getName(), StructureType.valueOf(type));
            structAdminService.getDraftStruct(player.getName()).ifPresent(struct -> {
                if (structAdminService.isStructValid(struct)) {
                    structService.create(struct, player::sendMessage);
                } else {
                    player.sendMessage("Name: " + struct.name());
                    player.sendMessage("Type: " + struct.type());
                    player.sendMessage("Loc1: " + (struct.getFirstLoc() != null));
                    player.sendMessage("Loc2: " + (struct.getFirstLoc() != null));
                }
            });
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());
        }
    }

    private void exportVolume(Player player, int structId, String volumeName) {
            structService.getStruct(structId)
                    .ifPresentOrElse(struct -> structService.exportVolume(struct, volumeName, player::sendMessage),
                () -> player.sendMessage(String.format("Struct with id %d not found", structId)));
    }
}