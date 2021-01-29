package com.minexd.zoot.network.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketStaffReport;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.Cooldown;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReportCommand {

    @Command(names = "report", async = true, permission = "")
    public static void report(Player player, @Param(name = "target") Player target, @Param(name = "reason", wildcard = true) String reason) {
        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "You cannot report yourself.");
            return;
        }
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
            return;
        }

        Zoot.get().getPidgin().sendPacket(new PacketStaffReport(
                ZootAPI.getColoredName(player),
                ZootAPI.getColoredName(target),
                reason,
                Bukkit.getServerId(),
                Bukkit.getServerName()
        ));

        profile.setRequestCooldown(new Cooldown(120_000L));
        player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());
    }

}
