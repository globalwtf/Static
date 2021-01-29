package com.minexd.zoot.essentials.command;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand {

    @Command(names = { "gamemode", "gm" }, permission = "static.gamemode", description = "Set a player's gamemode")
    public static void gamemode(final CommandSender sender, @Param(name = "mode", defaultValue = "-0*toggle*0-") final GameMode mode, @Param(name = "player", defaultValue = "self") final Player target) {
        run(sender, target, mode);
    }

    @Command(names = { "gms", "gm0" }, permission = "static.gamemode", description = "Set a player's gamemode to survival")
    public static void gms(final CommandSender sender, @Param(name = "player", defaultValue = "self") final Player target) {
        run(sender, target, GameMode.SURVIVAL);
    }

    @Command(names = { "gmc", "gm1" }, permission = "static.gamemode", description = "Set a player's gamemode to creative")
    public static void gmc(final CommandSender sender, @Param(name = "player", defaultValue = "self") final Player target) {
        run(sender, target, GameMode.CREATIVE);
    }

    @Command(names = { "gma", "gm2" }, permission = "static.gamemode", description = "Set a player's gamemode to adventure")
    public static void gma(final CommandSender sender, @Param(name = "player", defaultValue = "self") final Player target) {
        run(sender, target, GameMode.ADVENTURE);
    }

    private static void run(final CommandSender sender, final Player target, final GameMode mode) {
        if (!sender.equals(target) && !sender.hasPermission("static.gamemode.other")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        target.setGameMode(mode);
        if (!sender.equals(target)) {
            sender.sendMessage(target.getDisplayName() + ChatColor.GOLD + " is now in " + ChatColor.WHITE + mode.toString() + ChatColor.GOLD + " mode.");
        }
        target.sendMessage(ChatColor.GOLD + "You are now in " + ChatColor.WHITE + mode.toString() + ChatColor.GOLD + " mode.");
    }

}
