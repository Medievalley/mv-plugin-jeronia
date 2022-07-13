package org.shrigorevich.ml.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.models.User;
import org.shrigorevich.ml.domain.services.IStructureCreatorService;
import org.shrigorevich.ml.domain.services.IUserService;

import java.util.Optional;

public class StructureExecutor implements CommandExecutor {
    private final IStructureCreatorService structCreator;
    private final IUserService userService;

    public StructureExecutor(IStructureCreatorService structCreator, IUserService userService) {
        this.structCreator = structCreator;
        this.userService = userService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length > 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(args[0].equals("set")) {
                    if(args[1].equals("type")) {
                        try {
                            int typeId = Integer.parseInt(args[2]);
                            setType(player, typeId);
                        } catch (Exception ex) {
                            player.sendMessage(ChatColor.RED + "Wrong argument type (struct type id)");
                        }
                    } else if (args[1].equals("default")) {
                        try {
                            Optional<User> u = userService.getFromOnlineList(player.getName());
                            if (u.isPresent())
                                structCreator.createDefault(u.get(), ((result, msg) -> {
                                    player.sendMessage(msg);
                                }));
                            else
                                player.sendMessage("User not authorized");
                        } catch (IllegalArgumentException ex) {
                            Bukkit.getLogger().severe(ex.toString());
                            player.sendMessage(ChatColor.RED + ex.getMessage());
                        }
                    }
                }
            } else {
                System.out.println("You can`t use this command through console");
            }
        }
        return false;
    }

    private void setType(Player p, int typeId) {
        CreateStructModel m = structCreator.getStruct(p.getName());
        if (m == null) m = new CreateStructModel();
        StructureType t = null;
        for(StructureType st : StructureType.values()) {
            if (st.getTypeId() == typeId) {
                t = st;
                m.typeId = typeId;
            }
        }
        if (t == null)
            p.sendMessage(ChatColor.RED + "Wrong type id");
        else
            p.sendMessage(ChatColor.GREEN + "Structure type is: " + t.name());

    }

}