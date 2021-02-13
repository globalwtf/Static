package com.minexd.zoot.tokens.menus;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BackButton extends Button {
    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;
        new MainTokenMenu().openMenu(player);
    }

    @Override
    public String getName(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Back";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&cReturn to the Main Token Shop"));
        return lore;
    }
    @Override
    public Material getMaterial(Player player) {
        return Material.REDSTONE;
    }
}
