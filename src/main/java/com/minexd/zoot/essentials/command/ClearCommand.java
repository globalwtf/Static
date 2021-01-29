package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClearCommand {

    @Command(names = {"clear", "ci"}, permission = "static.clearinv")
    public static void clear(CommandSender commandSender, @Param(name = "target", defaultValue = "self") Player target) {
        target.getInventory().setContents(new ItemStack[36]);
        target.getInventory().setArmorContents(new ItemStack[4]);
        target.updateInventory();
        target.sendMessage(commandSender == target ? CC.GREEN + "You have cleared your inventory." : CC.GREEN + "Your inventory has been cleared by " + target.getName() + ".");
    }

}
