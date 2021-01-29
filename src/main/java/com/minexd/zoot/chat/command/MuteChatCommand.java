package com.minexd.zoot.chat.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand {

    @Command(names = {"mutechat", "mc"}, permission = "static.staff.mutechat")
    public static void mutechat(CommandSender sender) {
        Zoot.get().getChat().togglePublicChatMute();

        String senderName;

        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        String context = Zoot.get().getChat().isPublicChatMuted() ? "muted" : "unmuted";

        Bukkit.broadcastMessage(Locale.MUTE_CHAT_BROADCAST.format(context, senderName));
    }

}
