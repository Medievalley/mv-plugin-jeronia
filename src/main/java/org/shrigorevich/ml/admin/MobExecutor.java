package org.shrigorevich.ml.admin;

import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftMob;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.ai.TaskService;
import org.shrigorevich.ml.domain.ai.goals.GoGoal;
import org.shrigorevich.ml.domain.mob.MobService;
import org.shrigorevich.ml.domain.mob.events.SpawnPressureMobsEvent;
import org.shrigorevich.ml.domain.structure.StructureService;

public class MobExecutor implements CommandExecutor {

    private final MobService mobService;
    private final TaskService taskService;
    private final StructureService structureService;
    public MobExecutor(
            TaskService taskService,
            MobService mobService, StructureService structureService) {
        this.taskService = taskService;
        this.mobService = mobService;
        this.structureService = structureService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player player){
                try {
                    switch (args[0].toLowerCase()) {
                        case "c", "create" -> {
                            player.getWorld().spawnEntity(
                                player.getLocation().clone().add(5, 0, 0),
                                EntityType.valueOf(args[1]),
                                CreatureSpawnEvent.SpawnReason.CUSTOM, (e) -> {
                                    if (e instanceof Mob mob) {
//                                        ((CraftZombie) mob).getHandle();
                                    }
                                }
                            );

//                            Bukkit.getServer().getMobGoals().addGoal(this.customMob, new GoGoal());
                        }
                        case "pressure" -> Bukkit.getPluginManager().callEvent(new SpawnPressureMobsEvent());
                        default ->
                            player.sendMessage(ChatColor.YELLOW + String.format("Command '%s' not found", args[0]));
                    }
                }
                catch (Exception ex) {
                    Bukkit.getLogger().severe(ex.toString());
                    player.sendMessage(ChatColor.RED + ex.getMessage());
                }
            } else {
                System.out.println("You can`t use this command through console");
            }
        }
        return true;
    }

    private void setAttributes(Entity e, String arg2) {
        if (e instanceof Attributable atr) {
            if (atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(atr.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 2);
            if (atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                atr.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(Integer.parseInt(arg2));
            if (atr.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null)
                atr.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(atr.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 2);
            if (atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(atr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 2);
        }
    }

    private void setAI(Mob mob) {
        mob.getPathfinder().setCanOpenDoors(true);
        mob.getPathfinder().setCanPassDoors(true);
        MobGoals goals = Bukkit.getServer().getMobGoals();
        goals.removeAllGoals(mob);
    }
}