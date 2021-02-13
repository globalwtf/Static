package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankSetWeightCommand {

    @Command(names = "rank setweight", permission = "static.admin.rank", async = true)
    public static void weight(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "weight") int weight) {
        rank.setWeight(weight);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's weight.");
    }
}
