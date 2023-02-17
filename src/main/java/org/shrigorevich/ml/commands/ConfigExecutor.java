package org.shrigorevich.ml.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.config.ConfPath;
import org.shrigorevich.ml.config.MlConfiguration;


public class ConfigExecutor implements CommandExecutor {

    private final MlConfiguration config;
    private final Logger logger;

    public ConfigExecutor(MlConfiguration config) {
        this.config = config;
        this.logger = LogManager.getLogger("ConfigExecutor");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player player){
                try {
                    UpdateConfig(player, args[0], args[1]);
                }
                catch (Exception ex) {
                    logger.error(ex.getMessage());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                logger.error("You can`t use this command through console");
            }
        }
        return true;
    }

    private void UpdateConfig(Player player, String path, String value) {

        switch (ConfPath.getByPath(path)) {
            case MOB_SPAWN_MAX_MOB_QTY -> config.setMaxMobQty(Integer.parseInt(value));
            case MOB_SPAWN_PRESSURE_PF -> config.setPressurePlayersFactor(Double.parseDouble(value));
            case MOB_SPAWN_PRESSURE_INTERVAL -> config.setPressureInterval(Integer.parseInt(value));
            default -> {
                player.sendMessage(ChatColor.RED + String.format("The path you specified does not exist. Path: %s", path));
                return;
            }
        }
        config.save();
    }
}
