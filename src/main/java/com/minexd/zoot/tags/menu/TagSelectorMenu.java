package com.minexd.zoot.tags.menu;

import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.tags.menu.button.ClearTagButton;
import com.minexd.zoot.tags.menu.button.TagButton;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vape on 5/30/2020 at 12:09 PM.
 */
public class TagSelectorMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_AQUA + "Tags";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(4, new ClearTagButton());

        int i = 9;
        for (PlayerTag tag : TagManager.getTagMap().values()) {
            buttonMap.put(i++, new TagButton(tag));
        }

        for (int j = 0; j < size(buttonMap); j++) {
            if (buttonMap.containsKey(j)) continue;

        }

        return buttonMap;
    }
}