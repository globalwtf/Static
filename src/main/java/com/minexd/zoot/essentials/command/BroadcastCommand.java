package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class BroadcastCommand {

    @Command(names = {"broadcast", "bc", "announce"}, permission = "static.broadcast")
    public static void broadcast(CommandSender sender, @Flag(value = "r") boolean raw, @Param(name = "broadcast", wildcard = true) String broadcast) {
        String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
        Bukkit.broadcastMessage(CC.translate((!raw ? "&7[&3&lStatic&7] &r" : "") + message));
    }


}