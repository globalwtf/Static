package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class TeleportCommand {

    @Command(names = {"teleport", "tp"}, permission = "static.tp")
    public static void teleport(Player sender, @Param(name = "target") Player target) {
        sender.teleport(target);
        sender.sendMessage(CC.GREEN + "You have teleported to " + target.getName());
    }

}