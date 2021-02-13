package com.minexd.zoot.util.menu.pagination;

import com.minexd.zoot.util.ItemBuilder;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.frozenorb.qlib.menu.pagination.ViewAllPagesMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class PageInfoButton extends Button {

    private PaginatedMenu menu;

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Page Info";
    }

    @Override
    public List<String> getDescription(Player player) {
        int pages = menu.getPages(player);

        return Arrays.asList(
                ChatColor.YELLOW + "You are viewing page #" + menu.getPage() + ".",
                ChatColor.YELLOW + (pages == 1 ? "There is 1 page." : "There are " + pages + " pages."),
                "",
                ChatColor.YELLOW + "Middle click here to",
                ChatColor.YELLOW + "view all pages."
        );
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(getMaterial(player))
                .name(getName(player))
                .lore(getDescription(player))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            playNeutral(player);
        }
    }

}