package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketClearGrants;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ClearGrantsCommand {

    @Command(names = "cleargrants", permission = "static.admin.cleargrants", async = true)
    public static void cleargrants(CommandSender sender, @Param(name = "player") Profile profile) {
        profile.getGrants().clear();
        if (profile.getPlayer() != null)
            profile.setupBukkitPlayer(profile.getPlayer());
        profile.save();

        Zoot.get().getPidgin().sendPacket(new PacketClearGrants(profile.getUuid()));

        sender.sendMessage(ChatColor.GREEN + "Cleared grants of " + profile.getName() + "!");

    }
}
