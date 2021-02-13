package com.minexd.zoot.tokens.menus;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.chat.Chat;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.menu.GrantCreateMenu;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.stream.Collectors;

public class RankMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Token Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;
        for (Rank rank : Rank.getRanks().values().stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).collect(Collectors.toList())) {
            if (rank.getWeight() == 0) continue;
            if (rank.getWeight() > 55) continue;
            buttons.put(i++, new RankButton(rank));
            buttons.put(8, new BackButton());
        }
        return buttons;
    }

    @AllArgsConstructor
    private class RankButton extends Button {
        private Rank rank;

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            if (issuer.getTokens() < (rank.getWeight() * 22)) {
                player.sendMessage(ChatColor.RED + "You dont have enough tokens to purchase the " + rank.getDisplayName() + ChatColor.RED + " rank.");
                return;
            } if (issuer.getActiveRank() == rank) {
                return;
            }
            else {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setrank " + player.getName() + " " + rank.getDisplayName() + " perm tokens");
                issuer.setTokens(issuer.getTokens() - (rank.getWeight() * 15));
                issuer.save();
            }
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&aPurchase the " + rank.getColor() + rank.getDisplayName() + " &arank.");
        }

        @Override
        public byte getDamageValue(Player player) {
            switch (rank.getWeight()) {
                case 30:
                    return 2;
                case 35:
                    return 4;
                case 40:
                    return 9;
                case 45:
                    return 3;
                case 50:
                    return 11;
                case 55:
                    return 1;
                default:
                    return 0;
            }
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.WOOL;
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> lore = new ArrayList<>();
            lore.add("s");
            return lore;
        }
    }
}
