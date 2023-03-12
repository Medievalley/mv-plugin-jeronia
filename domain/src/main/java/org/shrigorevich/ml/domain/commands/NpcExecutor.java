package org.shrigorevich.ml.domain.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.admin.NpcAdminService;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.npc.NpcRole;
import org.shrigorevich.ml.domain.npc.NpcService;

public class NpcExecutor implements CommandExecutor {

    private final NpcService npcService;
    private final NpcAdminService npcAdminService;
    private final MobService mobService;
    private final TaskService taskService;
    public NpcExecutor(
            NpcService npcService,
            NpcAdminService npcAdminService,
            TaskService taskService,
            MobService mobService) {
        this.npcService = npcService;
        this.taskService = taskService;
        this.mobService = mobService;
        this.npcAdminService = npcAdminService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player player){
                try {
                    switch (args[0].toLowerCase()) {
                        case "c", "create" -> npcAdminService.getDraftNpc(player.getName()).ifPresentOrElse(npc -> {
                            if (npcAdminService.isNpcValid(npc)) {
                                try {
                                    npcService.commitNpc(npc);
                                } catch (Exception e) {
                                    player.sendMessage(e.getMessage());
                                }
                            } else {
                                player.sendMessage(
                                    String.format("Not all parameters are defined: %s", npc.getString()));
                            }
                        }, () -> player.sendMessage("npc not created"));
                        case "name", "n" -> {
                            npcAdminService.draftNpcSetName(player.getName(), args[1], player::sendMessage);
                        }
                        case "role, r" -> {
                            npcAdminService.draftNpcSetRole(
                                player.getName(), NpcRole.valueOf(args[1].toUpperCase()), player::sendMessage);
                        }
                        case "cl", "clear" -> npcService.unload();
                        case "size", "s" -> player.sendMessage("" + Bukkit.getWorld("world").getEntities().size());
                        case "reload" -> {
                            if (args.length > 1) {
                                int id = Integer.parseInt(args[1]);
                                npcService.reloadByStruct(id);
                            }
                            npcService.reload();
                        }
                        case "test" -> {
                            EntityType[] mobs = new EntityType[]{EntityType.ZOMBIE, EntityType.SKELETON};
                            player.getWorld().dropItemNaturally(
                                player.getLocation(),
                                mobService.getSkull(mobs[Integer.parseInt(args[1])]));
                        }
                        default ->
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
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