package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class TimeCommands {

    @Command(names = "day", permission = "static.time")
    public static void day(Player player) {
        player.setPlayerTime(6000L, false);
        player.sendMessage(CC.GREEN + "It's now day time.");
    }

    @Command(names = "night", permission = "static.time")
    public static void night(Player player) {
        player.setPlayerTime(18000L, false);
        player.sendMessage(CC.GREEN + "It's now night time.");
    }

    @Command(names = "sunset", permission = "static.time")
    public static void sunset(Player player) {
        player.setPlayerTime(12000, false);
        player.sendMessage(CC.GREEN + "It's now sunset.");
    }


}