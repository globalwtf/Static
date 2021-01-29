package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketAdminChat;
import com.minexd.zoot.network.packet.PacketStaffChat;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AdminChatCommand {

    @Command(names = {"adminchat", "ac"}, permission = "static.admin")
    public static void ac(Player player, @Param(name = "message", wildcard = true, defaultValue = "§") String message) {
        if(message.equals("§")) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.getStaffOptions().adminChatModeEnabled(!profile.getStaffOptions().adminChatModeEnabled());
            if (profile.getStaffOptions().adminChatModeEnabled())
                profile.getStaffOptions().staffChatModeEnabled(false);
            player.sendMessage(profile.getStaffOptions().adminChatModeEnabled() ?
                    CC.GREEN + "You are now talking in admin chat." : CC.RED + "You are no longer talking in admin chat.");
        }else {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());

            if (!profile.getStaffOptions().staffModeEnabled()) {
                player.sendMessage(CC.RED + "You are not in staff mode.");
                return;
            }

            Zoot.get().getPidgin().sendPacket(new PacketAdminChat(ZootAPI.getColoredName(player),
                    Bukkit.getServerId(), message));
        }
    }

}
