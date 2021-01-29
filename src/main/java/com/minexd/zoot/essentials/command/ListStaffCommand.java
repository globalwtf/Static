package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.lang.StringUtils.join;

@CommandMeta(label = {"liststaff", "ls", "stafflist"}, permission = "static.staff")
public class ListStaffCommand {

    public void executue(Player sender) {
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
                    playerNames.add(CC.translate(player.getDisplayName() + "&7[V]"));
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
