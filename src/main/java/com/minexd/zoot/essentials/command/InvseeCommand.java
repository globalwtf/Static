package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.BasicPlayerInventory;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.command.parameter.offlineplayer.OfflinePlayerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;

public class InvseeCommand {

    @Command(names = {"invsee", "inv"}, permission = "static.invsee", description = "Open a player's inventory")
    public static void invsee(final Player sender, @Param(name = "player") final OfflinePlayerWrapper wrapper) {
        wrapper.loadAsync(player -> {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }
            if (player.equals(sender)) {
                sender.sendMessage(ChatColor.RED + "You cannot invsee yourself.");
                return;
            }
            sender.openInventory(BasicPlayerInventory.get(player).getBukkitInventory());
            BasicPlayerInventory.getOpen().add(sender.getUniqueId());
        });
    }
}
