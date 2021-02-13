package com.minexd.zoot.tokens.menus;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.minecraft.util.com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public class MainTokenMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Token Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(2, new TagMenuButton());
        buttons.put(4, new RankMenuButton());
        buttons.put(6, new ChatColorMenuButton());

        return buttons;
    }

    @AllArgsConstructor
    private class TagMenuButton extends Button {

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile issuer = Profile.getByUuid(player.getUniqueId());

            new TagMenu().openMenu(player);
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&6Purchase Tags");
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
    private class RankMenuButton extends Button {

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile issuer = Profile.getByUuid(player.getUniqueId());

            new RankMenu().openMenu(player);
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&aPurchase Ranks");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            Profile viewer = Profile.getByUuid(player.getUniqueId());
            return Lists.newArrayList(
                    "",
                    CC.translate("&9Purchase any rank from tokens."),
                    "",
                    CC.translate("&7 You currently have &6" + viewer.getTokens() + " &7tokens"));
        }
    }

    private class ChatColorMenuButton extends Button {

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile issuer = Profile.getByUuid(player.getUniqueId());

            player.sendMessage(CC.translate("&cThis is still a work in progress, please be patient."));
        }

        @Override
        public String getName(Player player) {
            return CC.translate("&bPurchase ChatColor's");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public List<String> getDescription(Player player) {
            Profile viewer = Profile.getByUuid(player.getUniqueId());
            return Lists.newArrayList(
                    "",
                    CC.translate("&9Purchase any ChatColor from tokens."),
                    "",
                    CC.translate("&7 You currently have &6" + viewer.getTokens() + " &7tokens"));
        }
    }
}