package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class TeleportHereCommand {

    @Command(names = {"tphere", "s"}, permission = "static.tphere")
    public static void teleport(Player sender, @Param(name = "target") Player target) {
        target.teleport(sender);
        sender.sendMessage(CC.GREEN + "You have teleported " + target.getName() + " to yourself");
    }

}