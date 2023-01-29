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
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.structure.StructureType;
import org.shrigorevich.ml.domain.structure.TownInfra;
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
                    Optional<User> user = userService.getFromOnlineList(player.getName());
                    if (user.isPresent() && user.get().getRole().accessLevel() < UserRole.MODER.accessLevel())
                        throw new Exception("You do not have permission for this operation");

                    switch (args[0].toLowerCase()) {
                        case "restore", "r" -> restore(args[1]);
                        case "c", "create" -> create(player, args[1], args[2]);
                        case "sv", "save_volume" -> exportVolume(player, args[1]);
                        case "av", "apply_volume" -> applyVolume(args[1], args[2]);
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

    private void applyVolume(String structId, String volumeId) {
        structService.getStruct(Integer.parseInt(structId)).ifPresent(s -> {
            if(s instanceof TownInfra ti) {
                projectService.finalizeProject(ti.getId());
                structService.applyVolume(ti, Integer.parseInt(volumeId));

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
        structAdminService.getStructCorners(player.getName()).ifPresentOrElse(locs -> {
            if(locs.size() != 2)
                throw new IllegalArgumentException(
                    String.format("Specify the coordinates of two structure corners. Now defined: %d", locs.size()));

            structService.create(
                name, StructureType.getByName(type),
                locs.get(0), locs.get(1), player::sendMessage
            );
        }, () -> player.sendMessage("First specify struct corners"));
    }

    private void exportVolume(Player player, String volumeName) {
        structAdminService.getSelectedStruct(player.getName())
            .ifPresentOrElse(struct -> structService.exportVolume(struct, volumeName, player::sendMessage),
                () -> player.sendMessage("First choose a structure"));
    }
}