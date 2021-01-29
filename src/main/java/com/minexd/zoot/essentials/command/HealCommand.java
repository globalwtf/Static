package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand {

    @Command(names = "heal", permission = "static.heal")
    public static void heal(CommandSender sender, @Param(name = "target", defaultValue = "self") Player target) {
        target.setHealth(20.0);
        target.setFoodLevel(20);
        target.setSaturation(5.0F);
        target.updateInventory();

        if(target != sender) sender.sendMessage(CC.GREEN + "You have healed " + target.getName());
        else target.sendMessage(CC.GREEN + "You have been healed.");
    }

}