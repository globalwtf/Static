package com.minexd.zoot.profile.option.menu;

import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.ItemBuilder;
import com.minexd.zoot.util.TextSplitter;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public abstract class ProfileOptionButton extends Button {

    @Override
    public Material getMaterial(Player player) {
        return getButtonItem(player).getType();
    }

    @Override
    public String getName(Player player) {
        return getOptionName();
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(isEnabled(player) ? getEnabledItem(player) : getDisabledItem(player));

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(TextSplitter.split(40, getDescription(), CC.GRAY, " "));
        lore.add("");
        lore.add((isEnabled(player) ? CC.BLUE + StringEscapeUtils.unescapeJava(" » ") : "   ") + "&a" + getEnabledOption());
        lore.add((!isEnabled(player) ? CC.BLUE + StringEscapeUtils.unescapeJava(" » ") : "   ") + "&c" + getDisabledOption());
        lore.add("");
        lore.add("&3Click to toggle this option.");

        return itemBuilder.name(getOptionName())
                .lore(lore)
                .build();
    }

    public abstract ItemStack getEnabledItem(Player player);

    public abstract ItemStack getDisabledItem(Player player);

    public abstract String getOptionName();

    public abstract String getDescription();

    public abstract String getEnabledOption();

    public abstract String getDisabledOption();

    public abstract boolean isEnabled(Player player);

}
