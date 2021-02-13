package com.minexd.zoot.util.menu.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DisplayButton extends Button {

    private ItemStack itemStack;
    private boolean cancel;

    @Override
    public String getName(Player player) {
        return "";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.emptyList();
    }

    @Override
    public Material getMaterial(Player player) {
        return itemStack == null ? null : itemStack.getType();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.itemStack == null) {
            return new ItemStack(Material.AIR);
        } else {
            return this.itemStack;
        }
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return this.cancel;
    }

}