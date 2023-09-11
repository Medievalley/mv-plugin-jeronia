package org.shrigorevich.ml.domain.admin;

import net.kyori.adventure.audience.Audience;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shrigorevich.ml.common.config.MlConfiguration;
import org.shrigorevich.ml.domain.MlPlugin;
import org.shrigorevich.ml.domain.structures.Structure;

import java.util.Collection;
import java.util.List;

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
            if(sender instanceof Player p){
                try {
                    playSound(p.getLocation().clone(), args[1], Float.parseFloat(args[2]), Float.parseFloat(args[3]));
                }
                catch (Exception ex) {
                    p.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                logger.error("You can`t use this command through console");
            }
        }
        return true;
    }

    private void playSound(Location l, String sound, float volume, float pitch) {
        l.getWorld().playSound(l, Sound.valueOf(sound), volume, pitch);
    }

    private @Nullable Entity getNearbyEntity(Player p) {
        Collection<Entity> entities = p.getNearbyEntities( 15, 5, 15);
        if (entities.size() > 0) {
            double lastDistance = 10000; //TODO: magic value
            Entity nearest = null;
            for (Entity entity : entities) {
                double distance = p.getLocation().distance(entity.getLocation());
                if (lastDistance > distance) {
                    lastDistance = distance;
                    nearest = entity;
                }
            }
            return nearest;
        } else {
            return null;
        }
    }
}
