package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.npc.NpcService;

public class NpcExecutor implements CommandExecutor {

    private final NpcService npcService;
    private final TaskService taskService;
    public NpcExecutor(NpcService npcService, TaskService taskService) {
        this.npcService = npcService;
        this.taskService = taskService;
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
                            npcService.unload();
                            break;
                        case "size":
                        case "s":
                            player.sendMessage("" + Bukkit.getWorld("world").getEntities().size());
                            break;
                        case "reload":
                            if (args.length > 1) {
                                int id = Integer.parseInt(args[1]);
                                npcService.reload(id);
                            }
                            npcService.reload();
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