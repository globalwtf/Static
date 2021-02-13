package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankSetColorCommand {

    @Command(names = "rank setcolor", permission = "static.admin.rank", async = true)
    public static void color(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "color") ChatColor chatColor) {
        if (chatColor == null) {
            sender.sendMessage(CC.RED + "That color is not valid.");
            return;
        }

        rank.setColor(chatColor);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's color.");
    }
}
