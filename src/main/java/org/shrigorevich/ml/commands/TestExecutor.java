package org.shrigorevich.ml.commands;

import com.destroystokyo.paper.entity.ai.GoalType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.MlPlugin;
import org.shrigorevich.ml.config.ConfPath;
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
                    //
                    plugin.getScheduler().runTaskAsynchronously(plugin, () -> {
                        try {
                            Thread.sleep(1500);
                            player.sendMessage("Online: " + player.getServer().getOnlinePlayers().size());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
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
