package com.minexd.zoot.tags.menu.button;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vape on 5/30/2020 at 12:24 PM.
 */
public class ClearTagButton extends Button {
    @Override
    public String getName(Player player) {
        return CC.RED + "Remove Tag";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();

        lore.add(CC.RED + "Click to reset your tag.");


        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null) return;

        if (profile.getActiveTagId() == null) {
            player.sendMessage(Locale.PLAYER_TAG_NO_ACTIVE_TAG.format());
            return;
        }

        profile.setActiveTagId(null);
        profile.reloadTags();
        profile.save();

        playSuccess(player);
        player.sendMessage(Locale.PLAYER_TAG_CLEARED.format());
    }

    @Override
    public byte getDamageValue(Player player) {
        return 1;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.REDSTONE_BLOCK;
    }
}