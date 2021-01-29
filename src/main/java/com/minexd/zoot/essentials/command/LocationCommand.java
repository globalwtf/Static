package com.minexd.zoot.essentials.command;

import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LocationCommand {

    @Command(names = {"loc", "location"}, permission = "static.loc")
    public static void loc(Player player) {
        player.sendMessage(ChatColor.GOLD + "World: " + ChatColor.YELLOW + player.getLocation().getWorld().getName());
        player.sendMessage(ChatColor.GOLD + "XYZ: " + ChatColor.YELLOW + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ());
        player.sendMessage(ChatColor.GOLD + "Pitch: " + ChatColor.YELLOW + player.getLocation().getPitch() + ChatColor.GOLD + " Yaw: " + ChatColor.YELLOW + player.getLocation().getYaw());
    }

}