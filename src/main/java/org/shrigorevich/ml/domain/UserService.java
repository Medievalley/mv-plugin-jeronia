package org.shrigorevich.ml.domain;

import org.shrigorevich.ml.db.callbacks.IAccessCheckCallback;
import org.shrigorevich.ml.db.contexts.IUserContext;
import org.shrigorevich.ml.db.models.User;

import java.util.Optional;

public class UserService implements IUserService{

    IUserContext userContext;
    public UserService(IUserContext userContext) {
        this.userContext = userContext;
    }
    public void accessCheck(String userName, String ip, IAccessCheckCallback cb) {
        String nameMsg = "Player with the same name is already on the server!",
                regMsg = "You are not registered!",
                ipMsg = "You are logged in with a new IP address. To activate it, log in again on our website :)",
                livesNumberMsg = "Your character has no lives left:)",
                confirmedMsg = "You have not verified your email";

        User user = userContext.getByName(userName);

        System.out.printf("%s, %s, %s%n", user.getLastIp(), ip, user.getLastIp().equals(ip));
        if (user == null) {
            cb.onСheck(false, regMsg);
        }
        else if(!user.getLastIp().equals(ip)) {
            cb.onСheck(false, ipMsg);
        }
        else if(!user.isConfirmed()) {
            cb.onСheck(false, confirmedMsg);
        }
        else if (user.getLivesNumber() <= 0){
            cb.onСheck(false, livesNumberMsg);
        }
        else if (getByNameInMemory(userName).isPresent()) {
            cb.onСheck(false, nameMsg);
        }
        else {
            addInMemory(user);
        }
    }

    public Optional<User> getByNameInMemory(String name) {
        User user = userContext.getByNameInMemory(name);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    public void addInMemory(User user) {
        userContext.addInMemory(user);
    }
}
