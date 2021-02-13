package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankInheritCommand {

    @Command(names = "rank inherit", permission = "static.admin.rank", async = true)
    public static void inherit(CommandSender sender, @Param(name = "parent") Rank parent, @Param(name = "child") Rank child) {
        if (parent.canInherit(child)) {
            parent.getInherited().add(child);
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "You made the parent rank " + parent.getDisplayName() +
                    " inherit the child rank " + child.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "That parent rank cannot inherit that child rank.");
        }
    }

}
