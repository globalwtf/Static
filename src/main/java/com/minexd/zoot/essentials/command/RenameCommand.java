package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand {

    @Command(names = "rename", permission = "static.rename")
    public static void rename(Player player, @Param(name = "name", wildcard = true) String name) {
        if (player.getItemInHand() != null) {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(CC.translate(name));
            itemStack.setItemMeta(itemMeta);

            player.updateInventory();
            player.sendMessage(CC.GREEN + "You renamed the item in your hand.");
        } else {
            player.sendMessage(CC.RED + "There is nothing in your hand.");
        }
    }

}
