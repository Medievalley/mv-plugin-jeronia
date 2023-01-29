package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.config.ConfigurationImpl;
import org.shrigorevich.ml.domain.scoreboard.BoardType;

public class ConfigExecutor implements CommandExecutor {

    private final ConfigurationImpl config;

    public ConfigExecutor(ConfigurationImpl config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                try {
                    switch (args[0].toLowerCase()) {
                        case "project":
                            showScoreboard(player);
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

    private void showScoreboard(Player player) {
        player.setScoreboard(scoreboardService.getScoreboard(BoardType.PROJECT));
    }
}
