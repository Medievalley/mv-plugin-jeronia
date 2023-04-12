package org.shrigorevich.ml.state.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.callback.IAccessCheckCallback;
import org.shrigorevich.ml.common.callback.IResultCallback;
import org.shrigorevich.ml.common.config.MlConfiguration;
import org.shrigorevich.ml.domain.users.*;
import org.shrigorevich.ml.state.BaseService;

import java.util.*;

public class UserServiceImpl extends BaseService implements UserService {

    private final MlConfiguration config;
    private final Map<String, User> onlineList;
    private HashMap<Material, Restriction> restrictedItems;
    UserContext userContext;
    public UserServiceImpl(UserContext userContext, Plugin plugin, MlConfiguration config) {
        super(plugin, LogManager.getLogger("UserServiceImpl"));
        this.userContext = userContext;
        this.config = config;
        this.onlineList = new HashMap<>();
        this.restrictedItems = new HashMap<>();
    }

    @Override
    public void accessCheck(String userName, String ip, IAccessCheckCallback cb) {
        //TODO: Localize
        String nameMsg = "Player with the same name is already on the server!",
                ipMsg = "You are logged in with a new IP address. To activate it, log in again on our website :)",
                livesNumberMsg = "Your character has no lives left:)",
                confirmedMsg = "You have not verified your email",
                userNotValidMsg = "User not valid. Please contact support",
                userNotFoundMsg = "You are not registered!",
                serverErrorMsg = "Server error:( Try again or contact support";

        try {
            userContext.getByName(userName).ifPresentOrElse(model -> {
                if(!model.getIp().equals(ip)) {
                    cb.onCheck(false, ipMsg);
                }
                else if(!model.isVerified()) {
                    cb.onCheck(false, confirmedMsg);
                }
                else if (model.getLives() <= 0){
                    cb.onCheck(false, livesNumberMsg);
                }
                else if (getOnline(model.getName()).isPresent()) {
                    cb.onCheck(false, nameMsg);
                }
                else if (!isUserValid(model)){
                    cb.onCheck(false, userNotValidMsg);
                } else {
                    online(model);
                    cb.onCheck(true, "Allowed");
                }
            }, () -> cb.onCheck(false, userNotFoundMsg));
        } catch (Exception e) {
            cb.onCheck(false, serverErrorMsg);
        }
    }

    @Override
    public Optional<User> getOnline(String name) {
        User user = onlineList.get(name);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    private void online(UserModel model) {
        try {
            List<UserJobModel> userJobModelList = userContext.getUserJobsByUserId(model.getId());
            onlineList.put(model.getName(),
                    new UserImpl(model.getId(), model.getName(), UserRole.valueOf(model.getRoleId()), model.getLives(), userJobModelList));
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
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

    public void addUserJob(String userName, Job job, IResultCallback cb) {
        getOnline(userName).ifPresentOrElse(user -> {
            try {
                if(user.getJobs().size() >= config.getMaxJobsQty()) {
                    cb.sendResult(false,"You already work at max jobs!");
                    return;
                }
                else if (user.getJobs().contains(job)) {
                    cb.sendResult(false,"You already work at this job!");
                    return;
                }
                userContext.addUserJob(user.getId(), job.getJobId());
                user.addJob(job);
                cb.sendResult(true, String.format("You became a %s!", job.name().toLowerCase()));
            } catch (Exception e) {
                getLogger().error(e.getMessage());
                cb.sendResult(false,"Something went wrong :( Please contact the admin.");
            }
        }, () -> getLogger().error(String.format("User named %s is not in the online list", userName)));
    }

    public void removeUserJob(String userName, Job job, IResultCallback cb) {
        getOnline(userName).ifPresentOrElse(user -> {
            try {
                if (!user.getJobs().contains(job)) {
                    cb.sendResult(false,"You don't work at this job anyway!");
                    return;
                }
                userContext.removeUserJob(user.getId(), job.getJobId());
                user.removeJob(job);
                cb.sendResult(true, String.format("You quit your job as a %s!", job.name().toLowerCase()));
            } catch (Exception e) {
                getLogger().error(e.getMessage());
                cb.sendResult(false,"Something went wrong :( Please contact the admin.");
            }
        }, () -> getLogger().error(String.format("User named %s is not in the online list", userName)));
    }

    @Override
    public void setup(){
        try {
            List<RestrictedItemModel> restrictedItemModels = userContext.getRestrictedItems();
            setRestrictedItems(restrictedItemModels);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
    }

    private void setRestrictedItems(List<RestrictedItemModel> restrictedItemModels) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            for (RestrictedItemModel restrictedItemModel: restrictedItemModels) {
                List<JobAllowance> jobAllowances = new ArrayList<>();
                List<RoleAllowance> roleAllowances = new ArrayList<>();
                List<Allowance> allowances = new ArrayList<>();

                if(restrictedItemModel.getJobAllowances() != "[]")
                    jobAllowances = objectMapper.readValue(restrictedItemModel.getJobAllowances(), new TypeReference<List<JobAllowance>>(){});

                if(restrictedItemModel.getRoleAllowances() != "[]")
                    roleAllowances = objectMapper.readValue(restrictedItemModel.getRoleAllowances(), new TypeReference<List<RoleAllowance>>(){});

                allowances.addAll(jobAllowances);
                allowances.addAll(roleAllowances);

                restrictedItems.put(
                        Material.valueOf(restrictedItemModel.getType()),
                        new RestrictionImpl(allowances));
                getLogger().info("asd");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
    }
}
