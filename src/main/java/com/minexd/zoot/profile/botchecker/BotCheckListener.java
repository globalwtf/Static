package com.minexd.zoot.profile.botchecker;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.botchecker.menu.BotCheckerMenu;
import com.minexd.zoot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class BotCheckListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        System.out.println(profile.isBotChecked());
        if (!profile.isBotChecked() && profile.getActiveRank().getWeight() < 5) {
            new BotCheckerMenu().openMenu(profile.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMoveEvent(PlayerMoveEvent event) {
        if (event.getTo().distance(event.getFrom()) < 1)
            return;
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (!profile.isBotChecked() && profile.getActiveRank().getWeight() < 5) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (!profile.isBotChecked() && profile.getActiveRank().getWeight() < 5) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (!profile.isBotChecked() && profile.getActiveRank().getWeight() < 5) {
            profile.getPlayer().sendMessage(CC.translate("&cYou must still verify you are not a bot."));
            new BotCheckerMenu().openMenu(profile.getPlayer());
        }
    }
}
