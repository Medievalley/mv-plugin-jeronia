package org.shrigorevich.ml.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;

public class NpcExecutor implements CommandExecutor {

    private final NpcService npcService;
    private final MobService mobService;
    private final TaskService taskService;
    public NpcExecutor(NpcService npcService, TaskService taskService, MobService mobService) {
        this.npcService = npcService;
        this.taskService = taskService;
        this.mobService = mobService;
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
                            NpcRole role = NpcRole.valueOf(args[2].toUpperCase());
                            npcService.commitNpc(args[1], role, player.getName());
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
                                npcService.reloadByStruct(id);
                            }
                            npcService.reload();
                            break;

                        case "test":
                            EntityType[] mobs = new EntityType[] {EntityType.ZOMBIE, EntityType.SKELETON};

                            player.getWorld().dropItemNaturally(
                                    player.getLocation(),
                                    mobService.getSkull(mobs[Integer.parseInt(args[1])]));
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