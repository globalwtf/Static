package com.minexd.zoot.profile.menu;

import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.ItemBuilder;
import lombok.AllArgsConstructor;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ProfileMenuControlHeaderButton extends Button {

    private Profile profile;
    private RedisPlayerData playerData;

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public String getName(Player player) {
        return profile.getColoredUsername();
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(profile.getName());
        itemStack.setItemMeta(skullMeta);

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        if (playerData == null) {
            lore.add("&cNot available.");
        } else {
            if (playerData.getLastAction() == RedisPlayerData.LastAction.ONLINE) {
                lore.add("&aCurrently Online");
                lore.add("");
                lore.add("&3Server: &7" + playerData.getLastSeenServer());
                lore.add("&3Updated: &7" + "&aNow");
            } else if (playerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER) {
                lore.add("&cLast Seen");
                lore.add("");
                lore.add("&3Server: &7" + playerData.getLastSeenServer());
                lore.add("&3Updated: &7" + playerData.getLastSeenAt());
            } else if (playerData.getLastAction() == RedisPlayerData.LastAction.SWITCHING_SERVER) {
                lore.add("&3Switching Server");
                lore.add("");
                lore.add("&3Server: &7" + playerData.getLastSeenServer());
            }

        }

        lore.add(CC.MENU_BAR);

        return new ItemBuilder(itemStack)
                .name("&3" + profile.getName())
                .lore(lore)
                .build();
    }
}
