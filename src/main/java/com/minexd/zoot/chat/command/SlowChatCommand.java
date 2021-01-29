package com.minexd.zoot.chat.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlowChatCommand {

    @Command(names = {"slowchat", "slow"}, permission = "static.staff.slowchat")
    public static void slow(CommandSender sender, @Param(name = "seconds", defaultValue = "§") String duration) {
        if(duration.equals("§")) {
            Zoot.get().getChat().togglePublicChatDelay();

            String senderName;

            if (sender instanceof Player) {
                senderName = ZootAPI.getColoredName((Player) sender);
            } else {
                senderName = ChatColor.DARK_RED + "Console";
            }

            String context = Zoot.get().getChat().getDelayTime() == 1 ? "" : "s";

            if (Zoot.get().getChat().isPublicChatDelayed()) {
                Bukkit.broadcastMessage(Locale.DELAY_CHAT_ENABLED_BROADCAST.format(senderName,
                        Zoot.get().getChat().getDelayTime(), context));
            } else {
                Bukkit.broadcastMessage(Locale.DELAY_CHAT_DISABLED_BROADCAST.format(senderName));
            }
        } else {
            int seconds;
            try {
                seconds = Integer.parseInt(duration);
            }catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid duration. Please use 1-60 seconds.");
                return;
            }

            if (seconds < 0 || seconds > 60) {
                sender.sendMessage(ChatColor.RED + "A delay can only be between 1-60 seconds.");
                return;
            }

            String context = seconds == 1 ? "" : "s";

            sender.sendMessage(ChatColor.YELLOW + "You have updated the chat delay to " + seconds + " second" + context + ".");
            Zoot.get().getChat().setDelayTime(seconds);
        }
    }


}
