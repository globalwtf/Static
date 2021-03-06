package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankSetPrefixCommand {

    @Command(names = "rank setprefix", permission = "static.admin.rank", async = true)
    public static void setprefix(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "prefix", wildcard = true) String prefix) {
        rank.setPrefix(CC.translate(prefix));
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's prefix.");
    }

}
