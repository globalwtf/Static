package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPosCommand {

    @Command(names = { "tppos" }, permission = "static.tppos", description = "Teleport to coordinates")
    public static void teleport(Player sender, @Param(name = "x") double x, @Param(name = "y") double y, @Param(name = "z") double z) {
        if (isBlock(x)) {
            x += ((z >= 0.0) ? 0.5 : -0.5);
        }
        if (isBlock(z)) {
            z += ((x >= 0.0) ? 0.5 : -0.5);
        }
        sender.teleport(new Location(sender.getWorld(), x, y, z));
        String location = ChatColor.translateAlternateColorCodes('&', CC.WHITE +  x + ", " + y + ", " + z);

        sender.sendMessage(ChatColor.GOLD + "Teleporting to location " + location);
    }

    private static boolean isBlock(double value) {
        return value % 1.0 == 0.0;
    }

}