package com.minexd.zoot.tokens.menus;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.tags.menu.button.TagButton;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public class TagMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Token Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;
        for (PlayerTag tag : TagManager.getTagMap().values()) {
            if (i == 7) {
                i = 9;
            }
            buttons.put(i++, new TagTokenButton(tag));
            buttons.put(8, new BackButton());
        }
        return buttons;
    }

    @AllArgsConstructor
    private class TagTokenButton extends Button {
        private PlayerTag tag;

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile issuer = Profile.getByUuid(player.getUniqueId());
            if (issuer.getTokens() < (300)) {
                player.sendMessage(ChatColor.RED + "You dont have enough tokens to purchase the " + tag.getDisplayName() + ChatColor.RED + " tag.");
                return;
            } if (issuer.getOwnedTags().contains(tag)) {
                return;
            }
            else {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "tag give " + player.getName() + " " + tag);
                issuer.setTokens(issuer.getTokens() - (300));
                issuer.save();
            }
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&aPurchase the " + tag.getDisplayName() + " &atag.");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> lore = new ArrayList<>();
            lore.add("s");
            return lore;
        }
    }
}
