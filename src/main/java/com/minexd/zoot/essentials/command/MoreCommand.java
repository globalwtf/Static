package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class MoreCommand {

    @Command(names = "more", permission = "static.more")
    public static void more(Player player, @Param(name = "amount", defaultValue = "64") int amount) {
        if (player.getItemInHand() == null) {
            player.sendMessage(CC.RED + "There is nothing in your hand.");
            return;
        }
        if (amount == 64) {
            player.getItemInHand().setAmount(64);
        }
        else {
            player.getItemInHand().setAmount(Math.min(64, player.getItemInHand().getAmount() + amount));
        }
        player.updateInventory();
        player.sendMessage(CC.GREEN + "You gave yourself more of the item in your hand.");

    }

}