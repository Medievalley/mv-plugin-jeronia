package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.User;
import org.shrigorevich.ml.domain.services.IStructureService;
import org.shrigorevich.ml.domain.services.IUserService;

import java.util.Optional;

public class StructureExecutor implements CommandExecutor {
    private final IUserService userService;
    private final IStructureService structService;

    public StructureExecutor(IUserService userService, IStructureService structService) {
        this.userService = userService;
        this.structService = structService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                switch (args[0].toLowerCase()) {
                    case "find":
                        structService.getByIdAsync(Integer.parseInt(args[1]));
                        break;
                    case "create":
                        try {
                            Optional<User> u = userService.getFromOnlineList(player.getName());
                            if (u.isPresent())
                                structService.create(
                                        u.get(), args[1], args[2],
                                        Boolean.parseBoolean(args[3]),
                                        (result, msg) -> {
                                            //TODO: implement logic
                                        }
                                );
                            else
                                player.sendMessage(ChatColor.RED + "User not authorized");
                        } catch (Exception ex) {
                            Bukkit.getLogger().severe(ex.toString());
                            player.sendMessage(ChatColor.RED + ex.getMessage());
                        }
                        break;
                    default:
                        player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                        break;
                }
            } else {
                System.out.println("You can`t use this command through console");
            }
        }
        return true;
    }
}