package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketClearPunishments;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ClearPunishmentsCommand {

    @Command(names = "clearpunishments", permission = "static.admin.clearpunishments", async = true)
    public static void clearpunishments(CommandSender sender, @Param(name = "player") Profile profile) {
        if (profile == null) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.getPunishments().clear();
        profile.save();

        Zoot.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

        sender.sendMessage(ChatColor.GREEN + "Cleared punishments of " + profile.getColoredUsername() +
                ChatColor.GREEN + "!");
    }


}
