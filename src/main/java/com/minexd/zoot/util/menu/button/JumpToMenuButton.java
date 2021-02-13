package com.minexd.zoot.util.menu.button;

import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class JumpToMenuButton extends Button {

    private Menu menu;
    private ItemStack itemStack;

    public JumpToMenuButton(Menu menu, ItemStack itemStack) {
        this.menu = menu;
        this.itemStack = itemStack;
    }

    @Override
    public Material getMaterial(Player player) {
        return itemStack.getType();
    }

    @Override
    public String getName(Player player) {
        return "";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        menu.openMenu(player);
    }

}
