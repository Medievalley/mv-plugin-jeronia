package org.shrigorevich.ml.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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
                    UpdateConfig(args[1], args[2], args[3]);
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

    private void UpdateConfig(String section, String property, String value) throws Exception {
        switch (section.toLowerCase()) {
            case "mobspawn" -> updateMobSpawnSection(property, value);
            default -> throw new Exception("Section does not exist");
        }
    }

    private void updateMobSpawnSection(String property, String value) throws Exception {
        switch (property.toLowerCase()) {
            case "regspawninterval", "rsi" -> config.updateRegSpawnInterval(Integer.parseInt(value));
            default -> throw new Exception("Property does not exist");
        }
    }

}
