package com.minexd.zoot.tags.menu.button;

import com.google.common.collect.Lists;
import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.util.CC;
import lombok.RequiredArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vape on 5/30/2020 at 12:10 PM.
 */
@RequiredArgsConstructor
public class TagButton extends Button {

    private final PlayerTag tag;

    @Override
    public String getName(Player player) {
        return ChatColor.AQUA + tag.getDisplayName();
    }

    @Override
    public List<String> getDescription(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null)
            return Lists.newArrayList(CC.RED + "Failed to load.");

        List<String> lore = new ArrayList<>();

        lore.add(CC.GRAY + CC.STRIKE_THROUGH + "-----------------------------------");
        if(tag.getTag() == null || tag.getTag() == ""){
            lore.add(CC.AQUA + "Tag: " + CC.WHITE + "Null");
        }   else
                lore.add(CC.AQUA + "Tag: " + CC.RESET + ChatColor.translateAlternateColorCodes('&', tag.getTag()));
        if(tag.getDescription() != "" || tag.getDescription() != null)
            lore.add(CC.AQUA + "Description: " + CC.RESET + ChatColor.translateAlternateColorCodes('&', tag.getDescription()));
        else
            lore.add(CC.RED + "Null");
        lore.add("");

        if (!profile.getPermissions().contains("static.tag.all")) {
            if (!profile.getPermissions().contains("static.tag." + tag.getId().toLowerCase())) {
                lore.add(CC.RED + "You can purchase this tag on store.hcstatic.com");
            }
        } else {
            if (profile.isTagActive(tag))
                lore.add(CC.GREEN + "You already have this tag selected.");
            else
                lore.add(CC.GREEN + "Left click to choose this tag.");
        }
        lore.add(CC.GRAY + CC.STRIKE_THROUGH + "-----------------------------------");

        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null) return;

        if(!profile.getPermissions().contains("static.tag.all")) {
            if (!profile.getPermissions().contains("static.tag." + tag.getId().toLowerCase())) {
                player.sendMessage(Locale.PLAYER_TAG_NOT_OWNED.format());
                return;
            }
        }


        if (profile.isTagActive(tag)) {
            player.sendMessage(Locale.PLAYER_TAG_ALREADY_ENABLED.format());
            return;
        }

        profile.setActiveTagId(tag.getId());
        profile.reloadTags();
        profile.save();


        if(!profile.getPermissions().contains("static.tag.all")) {
            if (!profile.getPermissions().contains("static.tag." + tag.getId().toLowerCase())) {
                player.sendMessage(Locale.PLAYER_TAG_ENABLE_ERROR.format());
                return;
            }
        }

        playSuccess(player);
        player.sendMessage(Locale.PLAYER_TAG_ENABLED.format(tag.getId(), tag.getDisplayName(), ChatColor.translateAlternateColorCodes('&', tag.getTag())));
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }
}