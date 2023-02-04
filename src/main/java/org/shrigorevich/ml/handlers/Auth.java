package org.shrigorevich.ml.handlers;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shrigorevich.ml.domain.users.contracts.UserService;

public class Auth implements Listener {

    UserService userService;
    public Auth(UserService userService) {
        this.userService = userService;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void preLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().toString().replace("/", "");
        userService.accessCheck(event.getName(), ip, (isAllowed, msg) -> {
            if(!isAllowed) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(msg));
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Logout(PlayerQuitEvent event) {
        userService.removeFromOnlineList(event.getPlayer().getName());
    }
}