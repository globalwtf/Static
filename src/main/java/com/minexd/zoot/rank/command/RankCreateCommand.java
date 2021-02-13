package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankCreateCommand {

    @Command(names = "rank create", permission = "static.admin.rank", async = true)
    public static void create(CommandSender sender, @Param(name = "name") String name) {
        if (Rank.getRankByDisplayName(name) != null) {
            sender.sendMessage(CC.RED + "A rank with that name already exists.");
            return;
        }

        Rank rank = new Rank(name);
        rank.save();

        sender.sendMessage(CC.GREEN + "You created a new rank.");
    }


}
