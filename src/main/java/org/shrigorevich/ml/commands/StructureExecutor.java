package org.shrigorevich.ml.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;
import org.shrigorevich.ml.domain.services.IStructureCreatorService;

public class StructureExecutor implements CommandExecutor {
    private final IStructureCreatorService structCreator;

    public StructureExecutor(IStructureCreatorService structCreator) {
        this.structCreator = structCreator;
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