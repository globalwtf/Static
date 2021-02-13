package com.minexd.zoot.util.menu.button;

import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.ItemBuilder;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class BackButton extends Button {

    private Menu back;

    @Override
    public String getName(Player player) {
        return CC.RED + CC.BOLD + "Back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                CC.RED + "Click here to return to",
                CC.RED + "the previous menu.");
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.REDSTONE;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .name(getName(player))
                .lore(getDescription(player))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }

}
