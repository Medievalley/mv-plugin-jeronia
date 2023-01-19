package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.domain.project.contracts.ProjectService;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.users.models.UserModelImpl;
import org.shrigorevich.ml.domain.structure.contracts.StructureService;
import org.shrigorevich.ml.domain.users.contracts.UserService;
import org.shrigorevich.ml.events.FinalizeProjectEvent;

import java.util.Optional;

public class StructureExecutor implements CommandExecutor {
    private final UserService userService;
    private final StructureService structService;
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;

    public StructureExecutor(UserService userService, StructureService structService, ProjectService projectService, ScoreboardService scoreboardService) {
        this.userService = userService;
        this.structService = structService;
        this.projectService = projectService;
        this.scoreboardService = scoreboardService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                try {
                    switch (args[0].toLowerCase()) {
                        case "restore":
                        case "r":
                            structService.getById(Integer.parseInt(args[1])).ifPresent(ls -> {
                                ls.restore();
                                projectService.getProject(ls.getId()).ifPresent(project -> {
                                    Bukkit.getServer().getPluginManager().callEvent(new FinalizeProjectEvent(project));
                                });
                            });
                            break;
                        case "c":
                        case "create":
                            Optional<UserModelImpl> u = userService.getFromOnlineList(player.getName());
                            if (u.isPresent())
                                structService.create(
                                        u.get(), args[1], args[2],
                                        Boolean.parseBoolean(args[3]),
                                        (result, msg) -> {
                                            player.sendMessage(msg);
                                        }
                                );
                            else
                                player.sendMessage(ChatColor.RED + "User not authorized");
                            break;
                        case "sv":
                        case "save_volume":
                            String res = structService.exportVolume(player.getName(), args[1]);
                            player.sendMessage(res);
                            break;
                        case "av":
                        case "apply_volume":
                            Optional<LoreStructure> structure = structService.getById(Integer.parseInt(args[1]));
                            structure.ifPresent(loreStructure -> loreStructure.applyVolume(Integer.parseInt(args[2])));
                            break;
                        default:
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                            break;
                    }
                } catch (Exception ex) {
                    Bukkit.getLogger().severe(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                System.out.println("You can`t use this command through console");
            }
        }
        return true;
    }
}