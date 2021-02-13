package com.minexd.zoot.tags.command;

import com.minexd.zoot.tags.menu.TagSelectorMenu;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class TagsCommand {

    @Command(names = "tags", permission = "")
    public static void tags(Player player) {
        new TagSelectorMenu().openMenu(player);
    }
}
