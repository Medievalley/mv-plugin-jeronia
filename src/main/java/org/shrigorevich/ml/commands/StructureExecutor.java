package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.domain.structure.LoreStructure;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.services.StructureService;
import org.shrigorevich.ml.domain.users.IUserService;

import java.util.Optional;

public class StructureExecutor implements CommandExecutor {
    private final IUserService userService;
    private final StructureService structService;

    public StructureExecutor(IUserService userService, StructureService structService) {
        this.userService = userService;
        this.structService = structService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                try {
                    switch (args[0].toLowerCase()) {

                        case "delete":
                        case "d":
                            structService.delete(Integer.parseInt(args[1]));
                            break;
                        case "restore":
                        case "r":
                            Optional<LoreStructure> s = structService.getById(Integer.parseInt(args[1]));
                            if (s.isPresent()) {
                                LoreStructure ls = s.get();
                                ls.restore();
                            }
                            break;
                        case "c":
                        case "create":
                            Optional<User> u = userService.getFromOnlineList(player.getName());
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
                            structService.saveStructVolume(player.getName(), args[1], (res, msg) -> {
                                player.sendMessage(msg);
                            });
                            break;
                        case "av":
                        case "apply_volume":
                            structService.applyVolumeToStruct(Integer.parseInt(args[1]), Integer.parseInt(args[2]), (res, msg) -> {
                                player.sendMessage(msg);
                            });
                            break;
                        default:
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                            break;
                    }
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    Bukkit.getLogger().severe(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
                catch (IllegalArgumentException ex) {
                    Bukkit.getLogger().severe(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
                catch (Exception ex) {
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