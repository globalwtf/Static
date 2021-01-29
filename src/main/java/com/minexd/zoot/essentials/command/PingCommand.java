package com.minexd.zoot.essentials.command;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.util.BukkitReflection;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.StyleUtil;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand {

    @Command(names = "ping", permission = "")
    public static void ping(CommandSender sender, @Param(name = "target", defaultValue = "self") Player target) {
        if (sender == target)
            sender.sendMessage(CC.DARK_AQUA + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing((Player) sender)));
        else
            sender.sendMessage(ZootAPI.getColoredName(target) + CC.DARK_AQUA + "'s Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(target)));
    }


}
