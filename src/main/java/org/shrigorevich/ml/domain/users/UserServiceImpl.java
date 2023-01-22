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
        //TODO: Localize
        String nameMsg = "Player with the same name is already on the server!",
                regMsg = "You are not registered!",
                ipMsg = "You are logged in with a new IP address. To activate it, log in again on our website :)",
                livesNumberMsg = "Your character has no lives left:)",
                confirmedMsg = "You have not verified your email",
                userNotValidMsg = "User not valid. Please contact support",
                dataAccessErrorMsg = "Server error. Please try again or contact support";

        try {
            userContext.getByName(userName).ifPresentOrElse(user -> {
                if(!user.getIp().equals(ip)) {
                    cb.onСheck(false, ipMsg);
                }
                else if(!user.isVerified()) {
                    cb.onСheck(false, confirmedMsg);
                }
                else if (user.getLives() <= 0){
                    cb.onСheck(false, livesNumberMsg);
                }
                else if (getFromOnlineList(userName).isPresent()) {
                    cb.onСheck(false, nameMsg);
                }
                else if (!isUserValid(user)){
                    cb.onСheck(false, userNotValidMsg);
                } else {
                    addInOnlineList(
                        new UserImpl(user.getId(), user.getName(), UserRole.valueOf(user.getRoleId()), user.getLives()));
                }
            }, () -> cb.onСheck(false, regMsg));


        } catch (Exception ex) {
            cb.onСheck(false, dataAccessErrorMsg);
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

    private boolean isUserValid(UserModel user) {
        return UserRole.valueOf(user.getRoleId()) != null;
    }
}
