package com.minexd.zoot.rank;

import net.frozenorb.qlib.command.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankTypeAdapter implements ParameterType<Rank> {

    @Override
    public Rank transform(CommandSender commandSender, String s) {
        Rank rank = Rank.getRankByDisplayName(s);
        if(rank == null) commandSender.sendMessage(ChatColor.RED + "There is no such rank with the name \"" + s + "\".");
        return rank;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        List<String> completed = new ArrayList<>();

        for (Rank rank : Rank.getRanks().values()) {
            if (rank.getDisplayName().toLowerCase().startsWith(s.toLowerCase())) {
                completed.add(rank.getDisplayName());
            }
        }
        return completed;
    }
}
