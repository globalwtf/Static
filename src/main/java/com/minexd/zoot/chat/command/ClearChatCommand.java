package com.minexd.zoot.chat.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand {

    @Command(names = {"clearchat", "cc", "chatclear"}, permission = "static.staff.clearchat")
    public static void cc(CommandSender sender) {
        String[] strings = new String[101];

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("static.staff")) {
                if (Zoot.get().getMainConfig().getBoolean("CHAT.CLEAR_CHAT_FOR_STAFF")) {
                    player.sendMessage(strings);
                }
            } else {
                player.sendMessage(strings);
            }
        }

        String senderName;

        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        Bukkit.broadcastMessage(Locale.CLEAR_CHAT_BROADCAST.format(senderName));
    }

}
