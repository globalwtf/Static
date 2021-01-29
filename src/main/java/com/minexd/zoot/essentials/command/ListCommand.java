package com.minexd.zoot.essentials.command;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

public class ListCommand {

    @Command(names = "list", permission = "")
    public static void list(CommandSender sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort((o1, o2) -> {
            Profile p1 = Profile.getByUuid(o1.getUniqueId());
            Profile p2 = Profile.getByUuid(o2.getUniqueId());
            return p2.getActiveRank().getWeight() - p1.getActiveRank().getWeight();
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {
            String name = ZootAPI.getColoredName(player);

            if(player.hasMetadata("modmode") || player.hasMetadata("invisible")) {
                if (sender.hasPermission("static.staff"))
                    name = ZootAPI.getColoredName(player) + ChatColor.GRAY + " [V]";
                else
                    continue;
            }

            playerNames.add(name);
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getColor() + rank.getDisplayName());
        }

        int online = playerNames.size();

        sender.sendMessage(join(rankNames, ChatColor.WHITE + ", "));
        sender.sendMessage("(" + online + "/" + Bukkit.getMaxPlayers() + "): " +
                join(playerNames, ChatColor.WHITE + ", "));
    }

    @Command(names = {"liststaff", "ls", "stafflist"}, permission = "static.staff")
    public static void liststaff(CommandSender sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort((o1, o2) -> {
            Profile p1 = Profile.getByUuid(o1.getUniqueId());
            Profile p2 = Profile.getByUuid(o2.getUniqueId());
            return p2.getActiveRank().getWeight() - p1.getActiveRank().getWeight();
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {

            if (player.hasPermission("static.staff")) {
                if(player.hasMetadata("modmode")){
                    playerNames.add(CC.translate(player.getDisplayName() + "&7[&3V&7]"));
                } else {
                    playerNames.add(player.getDisplayName());
                }
            }
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getColor() + rank.getDisplayName());
        }

        int online = playerNames.size();

        sender.sendMessage(CC.SB_BAR);
        sender.sendMessage(CC.translate("&3Online Staff List"));
        sender.sendMessage(CC.SB_BAR);
        sender.sendMessage("(" + playerNames.size() + "): " +
                join(playerNames, ChatColor.GRAY + ", "));
    }

}
