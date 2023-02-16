package org.shrigorevich.ml.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.shrigorevich.ml.domain.users.contracts.UserService;

import java.io.Console;
import java.util.Objects;

public class PlayerDeathHandler implements Listener {
    private final UserService userService;
    private final Logger logger;

    public PlayerDeathHandler (UserService userService) {
        this.logger = LogManager.getLogger("PlayerDeathHandler");
        this.userService = userService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void OnPlayerDeath(PlayerDeathEvent event) {
        updateDeathStatistics(event.getPlayer());
        decrementUserLives(event.getPlayer());
    }

    private void updateDeathStatistics(Player player) {
        userService.updateDeathStatistics(player.getName(), Objects.requireNonNull(player.getLastDamageCause()).getCause().name());
    }

    private void decrementUserLives(Player player) {
        userService.decrementUserLives(player.getName());
    }
}
