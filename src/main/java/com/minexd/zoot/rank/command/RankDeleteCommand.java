package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankDeleteCommand {

    @Command(names = "rank delete", permission = "static.admin.rank", async = true)
    public static void delete(CommandSender sender, @Param(name = "rank") Rank rank) {
        rank.delete();
        sender.sendMessage(CC.GREEN + "You deleted the rank.");
    }


}
