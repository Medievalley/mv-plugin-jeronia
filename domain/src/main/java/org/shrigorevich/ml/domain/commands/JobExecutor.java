package org.shrigorevich.ml.domain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.users.UserJob;
import org.shrigorevich.ml.domain.users.UserService;

import java.util.Optional;

public class JobExecutor implements CommandExecutor {

    private final UserService userService;
    private final Logger logger;

    public JobExecutor(UserService userService) {
        this.userService = userService;
        this.logger = LogManager.getLogger("JobExecutor");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player player){
                try {
                    Optional<User> user = userService.getOnline(player.getName());

                    switch (args[0].toLowerCase()) {
                        case "join", "j" -> addUserJob(player, args[1]);
                        case "quit", "q" -> removeUserJob(player, args[1]);
                        case "me" -> getUserJobs(player);
                        default ->
                                player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                    }
                } catch (Exception ex) {
                    logger.error(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                logger.error("You can`t use this command through console");
            }
        }
        return true;
    }

    private void addUserJob(Player player, String jobName){
        try{
            userService.addUserJob(player.getName(), Job.valueOf(jobName.toUpperCase()), (result, msg) -> {
                if(result)
                    player.sendMessage(ChatColor.GREEN + msg);
                else
                    player.sendMessage(ChatColor.RED + msg);
            });
        }
        catch (IllegalArgumentException ex){
            player.sendMessage(ChatColor.RED + String.format("There is no job '%s' :(", jobName));
        }
    }

    private void removeUserJob(Player player, String jobName){
        try{
            userService.removeUserJob(player.getName(), Job.valueOf(jobName.toUpperCase()), (result, msg) -> {
                if(result)
                    player.sendMessage(ChatColor.GREEN + msg);
                else
                    player.sendMessage(ChatColor.RED + msg);
            });
        }
        catch (IllegalArgumentException ex){
            player.sendMessage(ChatColor.RED + String.format("There is no job '%s' :(", jobName));
        }
    }

    private void getUserJobs(Player player){
        userService.getOnline(player.getName()).ifPresent(user -> {
            String msg = "You work as a ";
            for (Job job: user.getJobs()) {
                msg += job.name() + ", ";
            }
            player.sendMessage(ChatColor.YELLOW + msg.substring(0, msg.length()-2));
        });
    }
}
