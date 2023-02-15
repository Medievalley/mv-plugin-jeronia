package org.shrigorevich.ml.domain.users;

import org.apache.logging.log4j.LogManager;
import org.bukkit.entity.EntityType;
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
    public Optional<UserModel> getUser(String userName) throws Exception {
        return userContext.getByName(userName);
    }

    @Override
    public void accessCheck(UserModel model, String ip, IAccessCheckCallback cb) {
        //TODO: Localize
        String nameMsg = "Player with the same name is already on the server!",
                ipMsg = "You are logged in with a new IP address. To activate it, log in again on our website :)",
                livesNumberMsg = "Your character has no lives left:)",
                confirmedMsg = "You have not verified your email",
                userNotValidMsg = "User not valid. Please contact support";

        if(!model.getIp().equals(ip)) {
            cb.onСheck(false, ipMsg);
        }
        else if(!model.isVerified()) {
            cb.onСheck(false, confirmedMsg);
        }
        else if (model.getLives() <= 0){
            cb.onСheck(false, livesNumberMsg);
        }
        else if (getOnline(model.getName()).isPresent()) {
            cb.onСheck(false, nameMsg);
        }
        else if (!isUserValid(model)){
            cb.onСheck(false, userNotValidMsg);
        } else {
            cb.onСheck(true, "Allowed");
        }
    }

    @Override
    public Optional<User> getOnline(String name) {
        User user = onlineList.get(name);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public void online(UserModel model) {

        onlineList.put(model.getName(),
            new UserImpl(model.getId(), model.getName(), UserRole.valueOf(model.getRoleId()), model.getLives()));
    }

    @Override
    public void offline(String name) {
        onlineList.remove(name);
    }

    private boolean isUserValid(UserModel user) {
        return UserRole.valueOf(user.getRoleId()) != null;
    }

    public void updateKillStatistics(String userName, EntityType entityType){
        getOnline(userName).ifPresentOrElse(user -> {
            try {
                userContext.updateKillStatistics(user.getId(), entityType.toString());
            } catch (Exception e) {
                getLogger().error(e.getMessage());
            }
        }, () -> getLogger().error(String.format("User named %s is not in the online list", userName)));
    }

    public void updateDeathStatistics(String userName, String deathReason) {
        getOnline(userName).ifPresentOrElse(user -> {
            try {
                userContext.updateDeathStatistics(user.getId(), deathReason);
            } catch (Exception e) {
                getLogger().error(e.getMessage());
            }
        }, () -> getLogger().error(String.format("User named %s is not in the online list", userName)));
    }

    public void decrementUserLives(String userName) {
        getOnline(userName).ifPresentOrElse(user -> {
            try {
                userContext.decrementUserLives(user.getId());
                user.removeLive();
            } catch (Exception e) {
                getLogger().error(e.getMessage());
            }
        }, () -> getLogger().error(String.format("User named %s is not in the online list", userName)));
    }
}
