package org.shrigorevich.ml.domain.users;

import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.callbacks.IAccessCheckCallback;
import org.shrigorevich.ml.domain.users.contracts.UserService;
import org.shrigorevich.ml.domain.users.contracts.User;
import org.shrigorevich.ml.domain.users.contracts.UserContext;
import org.shrigorevich.ml.domain.users.models.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl extends BaseService implements UserService {

    private final Map<String, User> onlineList;
    UserContext userContext;
    public UserServiceImpl(UserContext userContext, Plugin plugin) {
        super(plugin, LogManager.getLogger("UserServiceImpl"));
        this.userContext = userContext;
        this.onlineList = new HashMap<>();
    }

    @Override
    public void accessCheck(String userName, String ip, IAccessCheckCallback cb) {
        String nameMsg = "Player with the same name is already on the server!",
                regMsg = "You are not registered!",
                ipMsg = "You are logged in with a new IP address. To activate it, log in again on our website :)",
                livesNumberMsg = "Your character has no lives left:)",
                confirmedMsg = "You have not verified your email";

        try {
            Optional<UserModel> user = userContext.getByName(userName);

            if (user.isEmpty()) {
                cb.onСheck(false, regMsg);
            }
            else if(!user.get().getIp().equals(ip)) {
                cb.onСheck(false, ipMsg);
            }
            else if(!user.get().isVerified()) {
                cb.onСheck(false, confirmedMsg);
            }
            else if (user.get().getLives() <= 0){
                cb.onСheck(false, livesNumberMsg);
            }
            else if (getFromOnlineList(userName).isPresent()) {
                cb.onСheck(false, nameMsg);
            }
            else {
                addInOnlineList(new UserImpl(user.get()));
            }
        } catch (Exception ex) {
            getLogger().error(ex.toString());
        }
    }

    @Override
    public Optional<User> getFromOnlineList(String name) {
        User user = onlineList.get(name);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public void addInOnlineList(User user) {
        onlineList.put(user.getName(), user);
    }

    @Override
    public void removeFromOnlineList(String name) {
        onlineList.remove(name);
    }
}
