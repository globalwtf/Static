package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.BukkitReflection;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class SetSlotsCommand {

    @Command(names = "setslots", async = true, permission = "static.setslots")
    public static void setslots(CommandSender sender, @Param(name = "slots") Integer slots) {
        BukkitReflection.setMaxPlayers(Zoot.get().getServer(), slots);
        sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
    }

}
