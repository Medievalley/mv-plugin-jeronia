package org.shrigorevich.ml.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.MlConfiguration;


public class TestExecutor implements CommandExecutor {

    private final MlConfiguration config;
    private final MlPlugin plugin;
    private final Logger logger;

    public TestExecutor(MlConfiguration config, MlPlugin plugin) {
        this.plugin = plugin;
        this.config = config;
        this.logger = LogManager.getLogger("TestExecutor");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player player){
                try {
//                    CraftPlayer p = (CraftPlayer) player;

                }
                catch (Exception ex) {
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                logger.error("You can`t use this command through console");
            }
        }
        return true;
    }
}
