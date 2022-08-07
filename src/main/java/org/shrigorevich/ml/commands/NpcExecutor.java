package org.shrigorevich.ml.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.services.StructureService;
import org.shrigorevich.ml.domain.users.IUserService;

import java.util.List;

public class NpcExecutor implements CommandExecutor {

    private final NpcService npcService;
    public NpcExecutor(NpcService npcService) {
        this.npcService = npcService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                try {
                    switch (args[0].toLowerCase()) {
                        case "c":
                        case "create":
                            npcService.commitNpc(args[1], player.getName());
                            break;
                        case "cl":
                        case "clear":
                            break;
                        case "size":
                        case "s":
                            break;
                        default:
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                            break;
                    }
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