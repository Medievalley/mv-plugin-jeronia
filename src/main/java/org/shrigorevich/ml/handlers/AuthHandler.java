package org.shrigorevich.ml.handlers;

import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shrigorevich.ml.domain.users.contracts.UserService;
import org.shrigorevich.ml.domain.users.models.UserModel;

import java.util.Optional;

public class AuthHandler implements Listener {

    private final UserService userService;
    private final Logger logger;

    public AuthHandler(UserService userService) {
        this.userService = userService;
        this.logger = LogManager.getLogger("AuthHandler");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void preLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().toString().replace("/", "");
        try {
            userService.getUser(event.getName()).ifPresentOrElse(user -> {
                userService.accessCheck(user, ip, (isAllowed, msg) -> {
                    if(!isAllowed) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(msg));
                    }
                });
            }, () -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("You are not registered!")));
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("Server error. Please try again or contact support"));
            logger.error(e.getMessage());
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Logout(PlayerQuitEvent event) {
        userService.offline(event.getPlayer().getName());
    }
}