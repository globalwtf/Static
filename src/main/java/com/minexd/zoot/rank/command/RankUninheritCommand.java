package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankUninheritCommand {

    @Command(names = "rank uninherit", permission = "static.admin.rank", async = true)
    public static void uninherit(CommandSender sender, @Param(name = "parent") Rank parent, @Param(name = "child") Rank child) {
        if (parent.getInherited().remove(child)) {
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "You made the parent rank " + parent.getDisplayName() +
                    " no longer inherit the child rank " + child.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "That parent rank does not inherit that child rank.");
        }
    }


}
