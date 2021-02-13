package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RanksCommand {

    @Command(names = "ranks", permission = "static.admin.rank")
    public static void ranks(CommandSender sender) {
        List<Rank> ranks = new ArrayList<>(Rank.getRanks().values());
        ranks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());

        sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Ranks");

        for (Rank rank : ranks) {
            sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + rank.getColor() + rank.getDisplayName() +
                    ChatColor.RESET + " (Weight: " + rank.getWeight() + ")");
        }
    }

}
